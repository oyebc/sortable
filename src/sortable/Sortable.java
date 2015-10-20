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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oy
 */
public class Sortable {

    private final double THRESHOLD = 60.0;

    public static void main(String[] args) {

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
                            outputStream.write(currentLineListing.getBytes());
                        }
                        if (count2 == 2) {
                            break;
                        }
                    }
                    buffReaderListing.close();
                    instreamListing.close();
                     outputStream.flush();
                     outputStream.close();
                     
                     if(!matchedListings.isEmpty()){
                     Listing [] listings = new Listing[matchedListings.size()];
                     listings = matchedListings.toArray(listings);
                     String result= gson.toJson(new Result(currentProduct.getProduct_name(),listings));
                     resultOutputStream.write(result.getBytes());
                     }
                     count++;
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

    public double compare(Product product, Listing listing) {

        String currentProductString, currentListingString;

        currentProductString = this.sanitize(product.getManufacturer());
        currentListingString = this.sanitize(listing.getManufacturer());

        int manufacturerScore = currentListingString.contains(currentProductString) || currentProductString.contains(currentListingString) ? 40 : 0;

        currentListingString = this.sanitize(listing.getTitle());
        if (manufacturerScore == 0) {
            manufacturerScore = currentListingString.contains(currentProductString) ? 40 : 0;
        }
        currentProductString = this.sanitize(product.getProduct_name());

        int productNameScore = currentListingString.contains(currentProductString) ? 20 : 0;

        currentProductString = this.sanitize(product.getModel());
        int productModelScore = currentListingString.contains(currentProductString) ? 20 : 0;

        currentProductString = this.sanitize(product.getFamily());
        int productFamilyScore = currentListingString.contains(currentProductString) ? 20 : 0;
        // return "";
        double score = (manufacturerScore + productNameScore + productModelScore + productFamilyScore) * 1.0;

        return score;
    }
    
    private String sanitize(String dirtyString) {
        dirtyString = dirtyString.replaceAll("_", "").replaceAll("-", "").toLowerCase();
        return dirtyString.replaceAll("\\s", "");
    }    
}
