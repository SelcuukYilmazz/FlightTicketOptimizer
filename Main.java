import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Main {

    public static void main(String[] args)throws Exception{
//        Writer method
        FileWriter myWriter = new FileWriter("output.txt");

        Methods methods = new Methods();
        String st;
        String[] split = new String[0];
        String[] places = new String[0];


        List<Airport> vertex = new ArrayList<Airport>();

        int vertexNumber=1;

// Calculates vertex size here
        File file = new File(args[0]);

        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((st = br.readLine()) != null){
            split = st.split("\t");
            for(int i =1;i< split.length;i++){
                vertexNumber=vertexNumber+1;
            }
        }
// call graph class Constructor to construct a graph
        Graph graph = new Graph(vertexNumber);
//        Initialize airports
        file = new File(args[0]);

        br = new BufferedReader(new FileReader(file));

        while ((st = br.readLine()) != null){
            split = st.split("\t");
            for(int i =1;i< split.length;i++){
                vertex.add(new Airport(split[0],split[i]));
            }
        }

//      Initialize flights and adding edges
        file = new File(args[1]);

        br = new BufferedReader(new FileReader(file));


        while ((st = br.readLine()) != null){
            int startIndex = -1;
            int endIndex = -1;
            split = st.split("\t");
            places = split[1].split("->");


            for (int i = 0; i<vertex.size();i++){
                if (places[0].equals(vertex.get(i).getAlias())){
                    startIndex = i;
                }
                if (places[1].equals(vertex.get(i).getAlias())){
                    endIndex = i;
                }
            }
            graph.addEdge(startIndex,String.valueOf(endIndex),split[0]+" "+split[2]+" "+split[3]+" "+split[4]);
        }

//        Initialize commands
        String command;
        file = new File(args[2]);
        br = new BufferedReader(new FileReader(file));
        while ((st = br.readLine()) != null){
            split = st.split("\t");
            if (split.length>1){
                places = split[1].split("->");
            }
//          If command is listAll then prints all possible routes
            if (split[0].equals("listAll")){
                myWriter.write("command : "+split[0]+"\t"+split[1]+"\t"+split[2]+"\n");

                ArrayList<List<Integer>> Output=new ArrayList<List<Integer>>();
                ArrayList<ArrayList<String[]>> flightCodes=new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<Integer> price = new ArrayList<>();
                ArrayList<String[]> duration = new ArrayList<>();

                methods.calculateAllFlights(Output,flightCodes,dates,price,duration,vertex,graph,places,split);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date d1=sdf.parse("10/10/2010 10:23");

                methods.printFlight(Output,flightCodes,dates,price,duration,vertex,myWriter);
            }
//            If command is listProper then prints all proper flights that means both quicker and cheaper flights
            else if (split[0].equals("listProper")){
                myWriter.write("command : "+split[0]+"\t"+split[1]+"\t"+split[2]+"\n");

                ArrayList<List<Integer>> Output=new ArrayList<List<Integer>>();
                ArrayList<ArrayList<String[]>> flightCodes=new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<Integer> price = new ArrayList<>();
                ArrayList<String[]> duration = new ArrayList<>();

                methods.calculateAllFlights(Output,flightCodes,dates,price,duration,vertex,graph,places,split);

                methods.calculateProper(Output,flightCodes,dates,price,duration,vertex);
                methods.printFlight(Output,flightCodes,dates,price,duration,vertex,myWriter);
            }
            //            If command is listCheapest then prints all cheapest flights
            else if (split[0].equals("listCheapest")){
                myWriter.write("command : "+split[0]+"\t"+split[1]+"\t"+split[2]+"\n");

                ArrayList<List<Integer>> Output=new ArrayList<List<Integer>>();
                ArrayList<ArrayList<String[]>> flightCodes=new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<Integer> price = new ArrayList<>();
                ArrayList<String[]> duration = new ArrayList<>();

                methods.calculateAllFlights(Output,flightCodes,dates,price,duration,vertex,graph,places,split);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date d1=sdf.parse("10/10/2010 10:23");

                for (int i =0;i<Output.size();i++){
                    for (int j =0;j<Output.size();j++){
                        if (price.size()>1){
                            if (price.get(i)>price.get(j)){
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
                methods.printFlight(Output,flightCodes,dates,price,duration,vertex,myWriter);
            }
            //            If command is listQuickest then prints all quickest flights
            else if (split[0].equals("listQuickest")){
                myWriter.write("command : "+split[0]+"\t"+split[1]+"\t"+split[2]+"\n");

                ArrayList<List<Integer>> Output=new ArrayList<List<Integer>>();
                ArrayList<ArrayList<String[]>> flightCodes=new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<Integer> price = new ArrayList<>();
                ArrayList<String[]> duration = new ArrayList<>();

                methods.calculateAllFlights(Output,flightCodes,dates,price,duration,vertex,graph,places,split);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date d1=sdf.parse("10/10/2010 10:23");
                Date d2 = sdf.parse("10/10/2023 10:50");

                for (int i =0;i<Output.size();i++){
                    for (int j =0;j<Output.size();j++){
                        if (duration.size()>0){
                            for (int k =0;k<duration.size();k++){
                                if (flightCodes.get(i).get(0)[0].equals(duration.get(k)[0])){
                                    d1=sdf.parse(duration.get(k)[1]+" "+duration.get(k)[2]);
                                }
                                if (flightCodes.get(j).get(0)[0].equals(duration.get(k)[0])){
                                    d2=sdf.parse(duration.get(k)[1]+" "+duration.get(k)[2]);
                                }
                            }
                            Date d3 = sdf.parse(dates.get(i));
                            Date d4 = sdf.parse(dates.get(j));
                            long difference1 =d3.getTime()-d1.getTime();
                            long difference2 =d4.getTime()-d2.getTime();
                            int differenceminutes1 = (int) ((difference1 / (1000*60)));
                            int differenceminutes2 = (int) ((difference2 / (1000*60)));

                            if (differenceminutes1>differenceminutes2){

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
                methods.printFlight(Output,flightCodes,dates,price,duration,vertex,myWriter);
            }
            //            If command is listCheaper then prints all cheaper flights than given price
            else if (split[0].equals("listCheaper")){
                myWriter.write("command : "+split[0]+"\t"+split[1]+"\t"+split[2]+"\t"+split[3]+"\n");

                ArrayList<List<Integer>> Output=new ArrayList<List<Integer>>();
                ArrayList<ArrayList<String[]>> flightCodes=new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<Integer> price = new ArrayList<>();
                ArrayList<String[]> duration = new ArrayList<>();
                methods.calculateAllFlights(Output,flightCodes,dates,price,duration,vertex,graph,places,split);
                methods.calculateProper(Output,flightCodes,dates,price,duration,vertex);

                for (int i =0;i<Output.size();i++){
                    for (int j =0;j<Output.size();j++){

                            if (price.get(i) >= Integer.parseInt(split[3])){
                                Output.remove(i);
                                price.remove(i);
                                dates.remove(i);
                                flightCodes.remove(i);
                                j=-1;
                                i=0;
                            }

                    }

                }
                methods.printFlight(Output,flightCodes,dates,price,duration,vertex,myWriter);
            }
            //            If command is listQuicker then prints all quicker flights than given date
            else if (split[0].equals("listQuicker")){
                myWriter.write("command : "+split[0]+"\t"+split[1]+"\t"+split[2]+"\t"+split[3]+"\n");

                ArrayList<List<Integer>> Output=new ArrayList<List<Integer>>();
                ArrayList<ArrayList<String[]>> flightCodes=new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<Integer> price = new ArrayList<>();
                ArrayList<String[]> duration = new ArrayList<>();

                methods.calculateAllFlights(Output,flightCodes,dates,price,duration,vertex,graph,places,split);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                methods.calculateProper(Output,flightCodes,dates,price,duration,vertex);
                for (int i =0;i<Output.size();i++){
                    for (int j =0;j<Output.size();j++){
                            if (sdf.parse(dates.get(i)).compareTo(sdf.parse(split[3]))>=0){
                                Output.remove(i);
                                price.remove(i);
                                dates.remove(i);
                                flightCodes.remove(i);
                                j=-1;
                                i=0;
                            }
                    }

                }
                methods.printFlight(Output,flightCodes,dates,price,duration,vertex,myWriter);
            }
            //            If command is listExcluding then prints all flights that doesn't include given company
            else if (split[0].equals("listExcluding")){
                myWriter.write("command : "+split[0]+"\t"+split[1]+"\t"+split[2]+"\t"+split[3]+"\n");

                ArrayList<List<Integer>> Output=new ArrayList<List<Integer>>();
                ArrayList<ArrayList<String[]>> flightCodes=new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<Integer> price = new ArrayList<>();
                ArrayList<String[]> duration = new ArrayList<>();

                methods.calculateAllFlights(Output,flightCodes,dates,price,duration,vertex,graph,places,split);
                methods.calculateProper(Output,flightCodes,dates,price,duration,vertex);
                for (int i =0;i<Output.size();i++){
                    for (int k=0;k<flightCodes.get(i).size();k++){
                        if (split[3].equals(flightCodes.get(i).get(k)[0].substring(0,2))){
                            Output.remove(i);
                            price.remove(i);
                            dates.remove(i);
                            flightCodes.remove(i);
                            if (flightCodes.size()==0){
                                break;
                            }
                            i=0;
                            k=-1;
                        }
                    }
                }
                methods.printFlight(Output,flightCodes,dates,price,duration,vertex,myWriter);
            }
            //            If command is listOnlyFrom then prints all flights that only includes given company
            else if (split[0].equals("listOnlyFrom")){
                myWriter.write("command : "+split[0]+"\t"+split[1]+"\t"+split[2]+"\t"+split[3]+"\n");

                ArrayList<List<Integer>> Output=new ArrayList<List<Integer>>();
                ArrayList<ArrayList<String[]>> flightCodes=new ArrayList<>();
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<Integer> price = new ArrayList<>();
                ArrayList<String[]> duration = new ArrayList<>();

                methods.calculateAllFlights(Output,flightCodes,dates,price,duration,vertex,graph,places,split);
                methods.calculateProper(Output,flightCodes,dates,price,duration,vertex);
                for (int i =0;i<Output.size();i++){
                    for (int k=0;k<flightCodes.get(i).size();k++){
                        if (!split[3].equals(flightCodes.get(i).get(k)[0].substring(0,2))){
                            Output.remove(i);
                            price.remove(i);
                            dates.remove(i);
                            flightCodes.remove(i);
                            if (flightCodes.size()==0){
                                break;
                            }
                            i=0;
                            k=-1;
                        }
                    }
                }
                methods.printFlight(Output,flightCodes,dates,price,duration,vertex,myWriter);
            }
            //            I didn't implement this method so it just prints command name and Not implemented with 2 newline
            else if(split[0].equals("diameterOfGraph")){
                myWriter.write("command : "+split[0]+"\n");
                myWriter.write("Not implemented\n");
                myWriter.write("\n");
                myWriter.write("\n");
            }
            //            I didn't implement this method so it just prints command name and Not implemented with 2 newline
            else if(split[0].equals("pageRankOfNodes")){
                myWriter.write("command : "+split[0]+"\n");
                myWriter.write("Not implemented\n");
                myWriter.write("\n");
                myWriter.write("\n");
            }
        }
        myWriter.close();
    }
}