/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oy
 */
public class Sortable {

    private final double THRESHOLD = 85.0;

    public static void main(String[] args) {

//        String [] temp = new Sortable().sanitize("Canon PowerShot__D10 12.1 MP Waterproof Digital Camera with 3x Optical Image Stabilized Zoom and 2.5-inch LCD (Blue/Silver)");
//            Gson gson1 = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
//            Product  testProduct = gson1.fromJson("{\"product_name\":\"Canon\",\"manufacturer\":\"Canon\",\"model\":\"DSC-W310\",\"family\":\"Camera\",\"announced-date\":\"2010-01-06T19:00:00.000-05:00\"}", Product.class);
//        
//            Listing listing = gson1.fromJson("{\"title\":\"Canon PowerShot SX130IS 12.1 MP Digital Camera with 12x Wide Angle Optical Image Stabilized Zoom with 3.0-Inch LCD\",\"manufacturer\":\"Canon Canada\",\"currency\":\"CAD\",\"price\":\"199.96\"}", Listing.class);
//        
//            System.out.println(new Sortable().compare(testProduct, listing));
//            System.exit(0);
        try {
            //open file that contains products data
            Sortable sortable = new Sortable();
            FileInputStream instream = new FileInputStream("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\products.txt");
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(instream));

            String currentLine, currentLineListing;
            Product currentProduct;
            Listing currentListing;
            Gson gson;

            int count = 0;
            try {
                //open file that will contain results
                File resultFile = new File("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\results.txt");
                if (!resultFile.exists()) {
                    resultFile.createNewFile();
                }
                FileOutputStream resultOutputStream = new FileOutputStream(resultFile);
               

                while ((currentLine = buffReader.readLine()) != null) {

                    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
                    currentProduct = gson.fromJson(currentLine, Product.class);
                    ArrayList<Listing> matchedListings = new ArrayList<>();
                    
                    System.out.println("Product object: " + count);
                    System.out.println(currentProduct.toString());
                    System.out.println("******Listing objects*****");
                    int count2 = 0;
                    //TODO; check which file is smaller, listings.txt or listings_temp.txt, then the smaller is to be written to and the larger read from
                    File listingInput, file;
                    
                    
                    if (count % 2 == 0) {
                        listingInput = new File("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\listings.txt");
                        file = new File("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\listings_temp.txt");
                        if (!file.exists())       file.createNewFile();
                    
                    } else {
                        listingInput = new File("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\listings_temp.txt");
                        file = new File("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\listings.txt");
                    }
                    FileInputStream instreamListing = new FileInputStream(listingInput);
                    BufferedReader buffReaderListing = new BufferedReader(new InputStreamReader(instreamListing));

                    
                    FileOutputStream outputStream = new FileOutputStream(file);

                    while ((currentLineListing = buffReaderListing.readLine()) != null) {
                        Gson gsonList = new GsonBuilder().setPrettyPrinting().create();
                        currentListing = gsonList.fromJson(currentLineListing, Listing.class);
                        count2++;
                        System.out.println(currentListing.toString());

                        double score = sortable.compare(currentProduct, currentListing);
                        System.out.println("Score: "+score);
                        if (score >= sortable.THRESHOLD) {
                            matchedListings.add(currentListing);
                           // gsonList.toJson(new Result)
                            //
                        } else {
                            //String unmatched = gsonList.toJson(curren)
                            currentLineListing +="\n";
                            outputStream.write(currentLineListing.getBytes());
                            //outputStream.write
                        }
//                        if (count2 == 2) {
//                            break;
//                        }
                    }
                    buffReaderListing.close();
                    instreamListing.close();
                     outputStream.flush();
                     outputStream.close();
                     
                     if(!matchedListings.isEmpty()){
                     Listing [] listings = new Listing[matchedListings.size()];
                     listings = matchedListings.toArray(listings);
                     String result= gson.toJson(new Result(currentProduct.getProduct_name(),listings));
                     result+="\n";
                     resultOutputStream.write(result.getBytes());
                     }
                     count++;
//                    if (count == 5) {
//                        break;
//                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Sortable.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sortable.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public double compare(Product product, Listing listing) {
        
        List<String> currentListingStringArray;
        String[] currentProductStringArray;        
        
//        currentProductString = this.sanitize(product.getManufacturer());
//        currentListingString = this.sanitize(listing.getManufacturer());

        int totalNumOfWords=0, matchedWords=0, count=0;
        currentListingStringArray = Arrays.asList(this.sanitize(listing.getManufacturer()));
        currentProductStringArray = this.sanitize(product.getManufacturer());
        
        //int manufacturerScore = currentListingString.contains(currentProductString) || currentProductString.contains(currentListingString) ? 40 : 0;

        count = this.compareStringArrays(currentListingStringArray, currentProductStringArray);
        
        if(0<count)
        {
            matchedWords+=count;
            totalNumOfWords+=currentProductStringArray.length;
            currentListingStringArray = Arrays.asList(this.sanitize(listing.getTitle()));
        }
        else
        {
            currentListingStringArray = Arrays.asList(this.sanitize(listing.getTitle()));
            count = this.compareStringArrays(currentListingStringArray, currentProductStringArray);
            matchedWords+=count;
            totalNumOfWords+=currentProductStringArray.length;
            
        }              
        
        currentProductStringArray = this.sanitize(product.getProduct_name());
        count = this.compareStringArrays(currentListingStringArray, currentProductStringArray);
        matchedWords+=count;
        totalNumOfWords+=currentProductStringArray.length;
        
        
        currentProductStringArray = this.sanitize(product.getModel());
        count = this.compareStringArrays(currentListingStringArray, currentProductStringArray);
        matchedWords+=count;
        totalNumOfWords+=currentProductStringArray.length;

        currentProductStringArray = this.sanitize(product.getFamily());
        count = this.compareStringArrays(currentListingStringArray, currentProductStringArray);
        matchedWords+=count;
        totalNumOfWords+=currentProductStringArray.length;

        return (matchedWords*1.0/totalNumOfWords*1.0)*100;
    }
    
    public String[] sanitize(String dirtyString) {
        //dirtyString = dirtyString.replaceAll("_", "").replaceAll("-", "").toLowerCase();
        if(null==dirtyString)
        {return new String []{""};} 
        else {
            dirtyString = dirtyString.toLowerCase();
            String [] temp = dirtyString.split("-|\\s+|\\_");
            return temp;
        }
    }  
    
    public int compareStringArrays(List<String> listingArray, String[] productArray)
    {
//        System.out.println(Arrays.toString(productArray));
//        listingArray.stream().forEach((s) -> {
//            System.out.println(s);
//        });
        int count=0;
        for(String temp: productArray)
        {
            if(listingArray.contains(temp))
            {count++;
               // System.out.println(temp);
            }
        }
        
        return count;
    }
}
