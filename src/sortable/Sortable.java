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
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.java2d.pipe.OutlineTextRenderer;

/**
 *
 * @author Oy
 */
public class Sortable {

    private final double THRESHOLD= 60.0;
    
    public static void main(String[] args) {

        try {
            //open file that contains products data
            FileInputStream instream;
            BufferedReader buffReader;
            Sortable sortable = new Sortable();
            instream = new FileInputStream("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\products.txt");
            buffReader = new BufferedReader(new InputStreamReader(instream));
           
            String currentLine, currentLineListing;
            Product currentProduct;
            Listing currentListing;

            int count = 0;
            try {           
                //open file that will contain results
                File resultFile = new File("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\results.txt");
                if(!resultFile.exists())resultFile.createNewFile();
                FileOutputStream resultOutputStream = new  FileOutputStream(resultFile);
            
                while ((currentLine = buffReader.readLine()) != null) {

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
                    currentProduct = gson.fromJson(currentLine, Product.class);
                    count++;
//                    System.out.println("Product object: " + count);
//                    System.out.println(currentProduct.toString());
//                    System.out.println("******Listing objects*****");
                    int count2 = 0;
                    //TODO; check which file is smaller, listings.txt or listings_temp.txt, then the smaller is to be written to and the larger read from
                    FileInputStream instreamListing = new FileInputStream("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\listings.txt");
                    BufferedReader buffReaderListing = new BufferedReader(new InputStreamReader(instreamListing));
                    
                    File file = new File("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\listings_temp.txt");
                    if(!file.exists())file.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file);
                
                    while ((currentLineListing = buffReaderListing.readLine()) != null) {
                        Gson gsonList = new GsonBuilder().setPrettyPrinting().create();
                        currentListing = gsonList.fromJson(currentLineListing, Listing.class);
                        count2++;
                       // System.out.println(currentListing.toString());
                        
                        double score = sortable.compare(currentProduct,currentListing);
                        if (score >= sortable.THRESHOLD)
                        {
                            //
                        }
                        else
                        {
                            //String unmatched = gsonList.toJson(curren)
                            outputStream.write(currentLineListing.getBytes());
                        }
                        if (count2 == 2) {
                            break;
                        }
                    }

                    if (count == 5) {
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Sortable.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sortable.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private double compare(Product product, Listing listing) {
        
        String currentProductString, currentListingString;
        
        currentProductString = this.sanitize(product.getManufacturer());
        currentListingString = this.sanitize(listing.getManufacturer());         
        
       int manufacturerScore = currentListingString.contains(currentProductString)|| currentProductString.contains(currentListingString)? 40 : 0;
              
       currentListingString = this.sanitize(listing.getTitle());
       if(manufacturerScore==0){
            manufacturerScore = currentListingString.contains(currentProductString)? 40:0;
       }
       currentProductString = this.sanitize(product.getProduct_name());
       
       int productNameScore = currentListingString.contains(currentProductString)?20:0;
       
       currentProductString = this.sanitize(product.getModel());
       int productModelScore = currentListingString.contains(currentProductString)?20:0;
       
       currentProductString = this.sanitize(product.getFamily());
       int productFamilyScore = currentListingString.contains(currentProductString)?20:0;
       // return "";
       double score = (manufacturerScore + productNameScore + productModelScore + productFamilyScore)*1.0;
       
       return score;
    }

    private String sanitize(String dirtyString)
    {
        dirtyString = dirtyString.replaceAll("_","").replaceAll("-", "");
        return dirtyString.replaceAll("\\s", "");
    }
}
