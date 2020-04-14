package reviewdataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author AlbertSanchez
 */
public class ReviewDataset {

    private static String[] path = 
    {
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides1000/iOS0/",
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides1000/iOS1/",
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides1000/android0/",
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides1000/android1/",
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides1000/android2/"
    };
    private static String[] outPath = 
    {
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides500/iOS0/",
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides500/iOS1/",
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides500/android0/",
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides500/android1/",
        "/Users/AlbertSanchez/Desktop/Post/DatasetRides500/android2/"
    };
    private static long maxMS = 500;
        
    public static void main(String[] args) {

        if(path.length==outPath.length)
        {
            for (int i=0; i<path.length; i++)
            {
                List<String> files = getfiles(path[i]);

                List<FileCorrectness> fc = new ArrayList<>();
                for (String f : files)
                {
                    try{fc.add(getFileCorrectness(f));}
                    catch(Exception e)
                    {System.out.println("Something happened with file " + f + ". More info: " + e.getMessage());};
                }

                long okFiles = fc.stream().map(x->x).filter(y->y.isOkFile()==true).count();
                System.out.println(String.format("Good files rate: %d/%d",okFiles,fc.size()));

                System.out.println("Copying files");

                List<String> goodFiles = fc.stream().map(x->x).filter(y->y.isOkFile()==true).map(z->z.getNamefile()).collect(Collectors.toList());
                String name = "";
                int copiedFiles = 0;
                for (String namefile : goodFiles)
                {
                    name = namefile.replace(path[i], "");
                    if(copyFile(namefile,outPath[i]+name)) copiedFiles++;
                }

                System.out.println(String.format("Copied files: %d/%d",copiedFiles,okFiles));
            }
        }
        else System.out.println("Error: path and outPath must contain same number of items");
        
    }
    
    public static FileCorrectness getFileCorrectness(String file) throws FileNotFoundException, IOException
    {
        FileCorrectness fc = new FileCorrectness();
        int errors = 0;
        String[] incidentFields;
        long prevTs = 0l;
        List<Long> diferences = new ArrayList();
        
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        
        //System.out.println(file);

        String line = br.readLine(); //Read the 1st line which is <app version>#<file version>
        line = br.readLine(); //Read the 2nd line which are the headers
        line = br.readLine();
        
        while(!line.startsWith("=")) line = br.readLine(); 
        
        br.readLine(); //Read the 1st after '=' line which is <app version>#<file version>
        line = br.readLine(); //Read the 2nd after '=' line which are the headers
             
        if (line.equals("lat,lon,X,Y,Z,timeStamp,acc,a,b,c"))
        {
            fc.setOkFormat(true);
            line = br.readLine();
            if (line != null)
            {
                incidentFields = line.split(",",-1);
                prevTs = Long.parseLong(incidentFields[5]);
                line = br.readLine();

                while(line != null)
                {
                    incidentFields = line.split(",",-1);
                    if((Long.parseLong(incidentFields[5]) - prevTs) >= maxMS)
                    {
                        errors++;
                        diferences.add(Long.parseLong(incidentFields[5]) - prevTs);
                    }
                    prevTs = Long.parseLong(incidentFields[5]);
                    line = br.readLine();
                }
            }   
        }
        else fc.setOkFormat(false);
     
        fc.setNamefile(file);
        fc.setErrors(new Errors(errors,diferences));
        fc.setOkFile(errors==0 ? true : false);
        
        return fc;
    }
    
    public static boolean copyFile(String fromFile, String toFile) {
        File origin = new File(fromFile);
        File destination = new File(toFile);
        if (origin.exists()) {
            try {
                InputStream in = new FileInputStream(origin);
                OutputStream out = new FileOutputStream(destination);
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    
    public static List<String> getfiles(String path)
    {
        List<String> results = new ArrayList<>();
        File[] files = new File(path).listFiles();

        for (File file : files) 
        {
            if (file.isFile()) 
                if(!file.getName().startsWith(".") && file.getName().startsWith("VM"))
                    results.add(path + file.getName());
        }
        return results;
    }
}
