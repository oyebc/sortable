/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oy
 */
public class Sortable {

    public static void main(String[] args) {

        try {
            FileInputStream instream;
            BufferedReader buffReader;
            instream = new FileInputStream("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\products.txt");
            buffReader = new BufferedReader(new InputStreamReader(instream));

            FileInputStream instreamListing = new FileInputStream("C:\\Users\\Oy\\Documents\\NetBeansProjects\\Sortable\\src\\res\\listings.txt");
            BufferedReader buffReaderListing = new BufferedReader(new InputStreamReader(instreamListing));

            String currentLine, currentLineListing;
            Product currentProduct;
            Listing currentListing;

            int count = 0;
            try {
                while ((currentLine = buffReader.readLine()) != null) {

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
                    currentProduct = gson.fromJson(currentLine, Product.class);
                    count++;
//                    System.out.println("Product object: " + count);
//                    System.out.println(currentProduct.toString());
//                    System.out.println("******Listing objects*****");
                    int count2 = 0;
                    while ((currentLineListing = buffReaderListing.readLine()) != null) {
                        Gson gsonList = new GsonBuilder().setPrettyPrinting().create();
                        currentListing = gsonList.fromJson(currentLineListing, Listing.class);
                        count2++;
                       // System.out.println(currentListing.toString());
                        new Sortable().compare(currentProduct,currentListing);
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

    private void compare(Product product, Listing listing) {
        
        String currentProductString, currentListingString;
        
        currentProductString = this.sanitize(product.getManufacturer());
        currentListingString = this.sanitize(listing.getManufacturer());
        
        System.out.println(currentProductString);
        //System.out.println(currentListingString);
       // boolean isSameManufacturer = 
       // return "";
    }

    private String sanitize(String dirtyString)
    {
        //dirtyString = dirtyString.replaceAll("_", dirtyString);
        //dirtyString = dirtyString.replaceAll("-", dirtyString);
        return dirtyString.replaceAll("\\s", dirtyString);
    }
}
