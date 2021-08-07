// JAVA program to print all
// paths from a source to
// destination.
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

// Directed graph creation with using adjacency list
public class Graph {
    private int v;
    private ArrayList<ArrayList<String>>[] adjList;

    public Graph(int vertices)
    {
        this.v = vertices;

        initAdjList();
    }

//    Initialize adjaceny list here with given vertex number
    @SuppressWarnings("unchecked")
    private void initAdjList()
    {
        adjList = new ArrayList[v];

        for (int i = 0; i < v; i++) {
            adjList[i] = new ArrayList<ArrayList<String>>();
        }
    }
//      Add edge to adjacency list so it can create directed graph with this information
    public void addEdge(int u, String v, String weight)
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add(v);
        list.add(weight);
        adjList[u].add(list);
    }

//    This method calculates dates, total prices, Paths, Flight codes. This method basically calculates everything
//    It uses Calendar for date calculations and Arraylist for other things
    public ArrayList<List<Integer>> printAllPaths(int s, int d, String date, ArrayList<List<Integer>> Output,ArrayList<Integer> price,ArrayList<String> dates,ArrayList<ArrayList<String[]>> flightCodes,ArrayList<String[]> duration,ArrayList<String> visitedCities,List<Airport> vertex) throws ParseException {
        boolean[] isVisited = new boolean[v];
        ArrayList<Integer> pathList = new ArrayList<>();
        Calendar time = new GregorianCalendar();
        Calendar cal = new GregorianCalendar();
        ArrayList<String> previousTime = new ArrayList<>();

        ArrayList<String[]> tempFlight = new ArrayList<>();

//      It adds first vertex to pathlist because it is start point
        pathList.add(s);
        Output=printAllPathsUtil(s, d, isVisited, pathList,date, time,Output,price,0,dates,"00:00",cal,flightCodes,tempFlight,duration,visitedCities,vertex,previousTime);
        return Output;
    }
//    This method uses recursion. It visits all vertices if all rules are okay.
//    Rules are:
//    1- Next flight departure time must be later than arrival time.
//    2- It can't visit same city
//    3- It can't visit same vertex
//    4- If it went all the way destination then add this path to list and one by one go back
    private ArrayList<List<Integer>> printAllPathsUtil(Integer u, Integer d, boolean[] isVisited, List<Integer> localPathList,
                                                       String date,Calendar time,ArrayList<List<Integer>> Output,
                                                       ArrayList<Integer> price,Integer temp_price,
                                                       ArrayList<String> dates,
                                                       String current_time,Calendar cal,ArrayList<ArrayList<String[]>> flightCodes,
                                                       ArrayList<String[]> tempFlight,ArrayList<String[]> duration,ArrayList<String> visitedCities,List<Airport> vertex,ArrayList<String> previousTime) throws ParseException {


//        Simple Date Formats for calculation times
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat calendarDate = new SimpleDateFormat("dd/MM/yyyy");

        Date myDateTime = null;
//        If code comes here then it means code finds a way till the end. adds totalprice, end date and path to arraylist
        if (u.equals(d)) {

            if (!flightCodes.contains(tempFlight)){
                ArrayList<Integer> tempList = new ArrayList<Integer>(localPathList);
                Output.add(tempList);
                String tempCal =  simpleDateFormat.format(cal.getTime());

                dates.add(tempCal);
                ArrayList<String[]> copyList = new ArrayList<String[]>(tempFlight);
                flightCodes.add(copyList);
                price.add(temp_price);
            }
            // if match found then no need to traverse more till depth

            return Output;
        }

        // Marks the current node
        isVisited[u] = true;

        // Recur for all the vertices
        // adjacent to current vertex

        for (int k =0;k<adjList[u].size();k++) {
            ArrayList<String> i = adjList[u].get(k);
//            Initialize weight here that means flight codes, flights dates etc.
            String[] weight = i.get(1).split(" ");
//            Initialize dates here with above informations
            Calendar startDate = new GregorianCalendar();
            Calendar flightDate = new GregorianCalendar();
            startDate.setTime(calendarDate.parse(date));
            flightDate.setTime(calendarDate.parse(weight[1]));
//          If in path there is only one vertex then it must be equal to given start date. if else then it can go in
            if (localPathList.size()>1 || ( flightDate.compareTo(startDate)>=0 && localPathList.size()==1)){
//                If there is only one vertex then initialize rules. (Visited cities, arrival/departure times)
                if (localPathList.size()==1){
                    current_time = weight[1]+" "+weight[2];
                    myDateTime = simpleDateFormat.parse(current_time);
                    time.setTime(myDateTime);
                    duration.add(weight);
                    visitedCities.add(vertex.get(u).getCity_name());

                }

//              Initialize next flight departure time
                Calendar comparing_time = new GregorianCalendar();
                comparing_time.setTime(simpleDateFormat.parse(weight[1]+" "+weight[2]));


//              If vertex Not visited and arrival time before departure time unless it is not one vertex and city not visited then go in
                if (!isVisited[Integer.parseInt(i.get(0))]  && (time.compareTo(comparing_time) < 0 ||localPathList.size()==1) && !visitedCities.contains(vertex.get(Integer.parseInt(i.get(0))).getCity_name())) {
//              set time to departure time of next flight
                    current_time = weight[1]+" "+weight[2];
                    myDateTime = simpleDateFormat.parse(current_time);
                    time.setTime(myDateTime);
//              add city to visited cities
                    visitedCities.add(vertex.get(Integer.parseInt(i.get(0))).getCity_name());
//              add time duration of next flight
                    String[] times = weight[4].split(":");
                    time.add(Calendar.HOUR_OF_DAY,Integer.parseInt(times[0]));
                    time.add(Calendar.MINUTE,Integer.parseInt(times[1]));
//              add this time to arrival times list (previousTime)
                    String tempCal =  simpleDateFormat.format(time.getTime());
                    previousTime.add(tempCal);


                    current_time = weight[1]+" "+weight[2];
                    myDateTime = simpleDateFormat.parse(current_time);
                    cal.setTime(myDateTime);
                    cal.add(Calendar.HOUR_OF_DAY,Integer.parseInt(times[0]));
                    cal.add(Calendar.MINUTE,Integer.parseInt(times[1]));

                    temp_price = temp_price+Integer.parseInt(weight[5]);
                    localPathList.add(Integer.parseInt(i.get(0)));
                    tempFlight.add(weight);
                    Output = printAllPathsUtil(Integer.parseInt(i.get(0)), d, isVisited, localPathList,date,time,Output,price,temp_price,dates,current_time,cal,flightCodes,tempFlight,duration,visitedCities,vertex,previousTime);

                    // remove current node and all other temporary lists one by one
                    // in path[]
                    if (tempFlight.size()!=0){
                        tempFlight.remove(tempFlight.size()-1);
                    }
                    previousTime.remove(previousTime.size()-1);
                    if (previousTime.size()!=0){
                        time.setTime(simpleDateFormat.parse(previousTime.get(previousTime.size()-1)));
                    }

                    localPathList.remove(localPathList.size()-1);
                    visitedCities.remove(visitedCities.size()-1);
                    if (localPathList.size()==1){
                        temp_price = 0;
                    }
                    else{
                        temp_price = temp_price-Integer.parseInt(weight[5]);
                    }
                }
            }

        }

        // Mark the current node
        isVisited[u] = false;
        return Output;
    }



}