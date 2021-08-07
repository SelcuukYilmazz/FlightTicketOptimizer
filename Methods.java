import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Methods {

//    This method prints all calculated informations to text

    public void printFlight(ArrayList<List<Integer>> Output, ArrayList<ArrayList<String[]>> flightCodes, ArrayList<String> dates, ArrayList<Integer> price, ArrayList<String[]> duration, List<Airport> vertex, Writer myWriter) throws ParseException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date d1=sdf.parse("10/10/2010 10:23");
        Date d2 = sdf.parse("10/10/2023 10:50");
        if(Output.size()!=0){
            for (int i =0;i<Output.size();i++){
                for (int j=0;j<flightCodes.get(i).size();j++){
                    myWriter.write(flightCodes.get(i).get(j)[0]+"\t"+vertex.get(Output.get(i).get(j)).getAlias()+"->"+vertex.get(Output.get(i).get(j+1)).getAlias());
                    if (j+1!=flightCodes.get(i).size()){
                        myWriter.write("||");
                    }
                }

//                Calculates total flight duration with subtracting 2 dates
                for (int j =0;j<duration.size();j++){
                    if (flightCodes.get(i).get(0)[0].equals(duration.get(j)[0])){
                        d1=sdf.parse(duration.get(j)[1]+" "+duration.get(j)[2]);
                    }
                }
                d2 = sdf.parse(dates.get(i));
                long difference =d2.getTime()-d1.getTime();
                int differenceminutes = (int) ((difference / (1000*60)) % 60);
                int differencehours = (int) ((difference / (1000*60*60)));

                String a = " ";

                if (differencehours<10){

                    if (differenceminutes<10){
                        a = "0"+differencehours+":"+"0"+differenceminutes;
                    }else{
                        a = "0"+differencehours+":"+differenceminutes;
                    }
                }else{
                    if (differenceminutes<10){
                        a = differencehours+":"+"0"+differenceminutes;
                    }else{
                        a = differencehours+":"+differenceminutes;
                    }
                }

                myWriter.write("\t"+a+"/"+price.get(i)+"\n");
            }
        }
//        I there is no flight then it writes there is no suitable flight
        else{
            myWriter.write("No suitable flight plan is found\n");
        }
        myWriter.write("\n");
        myWriter.write("\n");
    }

//    This method calculates all proper flights we need this method because most of the methods using proper flights
    public void calculateProper(ArrayList<List<Integer>> Output,ArrayList<ArrayList<String[]>> flightCodes,ArrayList<String> dates,ArrayList<Integer> price,ArrayList<String[]> duration,List<Airport> vertex) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date d1=sdf.parse("10/10/2010 10:23");
        Date d2 = sdf.parse("10/10/2023 10:50");

        for (int i =0;i<Output.size();i++){
            for (int j =0;j<Output.size();j++){


                for (int k =0;k<duration.size();k++){
                    if (flightCodes.get(i).get(0)[0].equals(duration.get(k)[0])){
                        d1=sdf.parse(duration.get(k)[1]+" "+duration.get(k)[2]);
                    }
                    if (flightCodes.get(j).get(0)[0].equals(duration.get(k)[0])){
                        d2=sdf.parse(duration.get(k)[1]+" "+duration.get(k)[2]);
                    }
                }
//                Calculates total flight duration with subtracting 2 dates
                Date d3 = sdf.parse(dates.get(i));
                Date d4 = sdf.parse(dates.get(j));
                long difference1 =d3.getTime()-d1.getTime();
                long difference2 =d4.getTime()-d2.getTime();
                int differenceminutes1 = (int) ((difference1 / (1000*60)));
                int differenceminutes2 = (int) ((difference2 / (1000*60)));


                if (differenceminutes1>differenceminutes2 && price.get(i)>price.get(j)){
                    Output.remove(i);
                    price.remove(i);
                    dates.remove(i);
                    flightCodes.remove(i);
                    j=-1;
                    i=0;
                }
            }
        }
    }

//    This method calculates all possible flights
    public void calculateAllFlights(ArrayList<List<Integer>> Output,ArrayList<ArrayList<String[]>> flightCodes,ArrayList<String> dates,ArrayList<Integer> price,ArrayList<String[]> duration,List<Airport> vertex,Graph graph,String[] places,String[] split) throws ParseException {
        for(int i =0;i<vertex.size();i++){
            if (vertex.get(i).getCity_name().equals(places[0])){
                for (int j=0;j<vertex.size();j++){
                    if (vertex.get(j).getCity_name().equals(places[1])){
                        ArrayList<String> visitedCities = new ArrayList<>();
                        Output = graph.printAllPaths(i,j ,split[2],Output,price,dates,flightCodes,duration,visitedCities,vertex);
                    }
                }
            }
        }
    }
}
