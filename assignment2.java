/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;

import java.io.*; //for file input output
import java.util.Scanner; //scanner class use to scan user's input
import java.util.regex.Pattern; //for ic format pattern

/**
 *
 * @author Akatsuki
 */

public class assignment2 {
    static Scanner sc = new Scanner (System.in);
    static File file;
    static Scanner input;
    static String fileName = "SampleInput.txt"; //input file name
    public static void main (String[]args)
    {
      try{
        final int size = NoOfRecord(fileName); //get the number of records in the input file
        String[] name = new String[size];
        String[] identityCardNo = new String[size];
        String[] bloodType = new String[size];
        String[] weightStatus = new String[size];
        String[] dob = new String[size];
        String[] gender = new String[size];
        double[] weight = new double[size];
        double[] height = new double[size];
        double[] bmi = new double[size];
        readData(name,identityCardNo,bloodType); //read data from file
        nameValidation(name); //name from file validation
        ICValidation(identityCardNo); //IC from file validation
        bloodTypeValidation(bloodType); //bloodtype from file validation
        keyInHeight (height, name); //key in height and validation
        keyInWeight (weight, name); //key in weight and validation
        extractDOBfromIC(identityCardNo, dob); //extract dob from IC
        extractGenderfromIC(identityCardNo, gender); //extract gender from IC
        calculateBMI(height, weight, bmi); //calculate BMI
        assignWeightStatus(bmi, size, weightStatus); //assign weight status according to BMI
        double heightMean = calculateHeightMean(height); //calculate the height mean for the group of patients
        double heightSD = calculateHeightSD(height,heightMean); //calculate the height standard deviation for the group of patients
        double weightMean = calculateWeightMean(weight); //calculate the weight mean for the group of patients
        double weightSD = calculateWeightSD(weight,weightMean); //calculate the weight standard deviation for the group of patients
        String highest = findHighestPatient(height, name); //name for highest patients
        String heaviest = findHeaviestPatient(weight, name); //name for heaviest patients
        writeData(size, name, identityCardNo, bloodType, weightStatus, dob, gender, 
                height, weight, bmi, heightMean, heightSD, weightMean, weightSD, highest, heaviest); /*write record of each patient, 
        the mean and standard deviation for the group of patients, and name of the highest and the heaviest patients into a text file*/
      }
        catch(Exception e){
          //as error message already prompted by other function, so nothing here
      }  
    }
      public static int NoOfRecord(String fileName) //get the number of records in the input file
      {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) 
        {
            while (reader.readLine() != null) 
            {
                lines++;
            }
        }catch (Exception e) 
        {
          //as error message already prompted by other function, so nothing here
        }
        return lines;
      }
    public static void readData(String[] name,String[] identityCardNo,String[] bloodType) //read data from file
    {   
        file = new File(fileName);
        int i = 0;
        try{
        //make link
        if (!file.exists())
        {
            System.out.println(fileName+" does not exist.");
        }
        input = new Scanner(file); //load the file into the scanner (input)
        //process data
        while(input.hasNext())
        {
            String line = input.nextLine();
            String[] parts = line.split("#");
            name[i] = parts[0];
            identityCardNo[i] = parts[1];
            bloodType[i] = parts[2];
            i++; 
        }
        input.close(); //close link
        }
        //NullPointerException, InputMismatchException, FileNotFoundException
        catch(Exception e){
            System.out.println("Something wrong");
        }
    }
    public static void nameValidation(String[] name) //name from file validation
    {
        
        boolean validName=false;
        for(int i=0;i<name.length;i++)
        {
            do
            {
            //validation for name
            if (name[i].isBlank()) //check if the name input is blank
                {
                    System.out.print("Invalid name for Record "+(i+1)+", it should not be blank.\n");
                    validName=false;
                }
            else
                {
                    for (int j=0;j<name[i].length();j++) //checking if the name only contain space and alphabet
                    {
                        char character = name[i].charAt(j);
                        if (!Character.isLetter(character) && !Character.isWhitespace(character) && character!='/')  
                            {
                                System.out.print("Invalid name for Record "+(i+1)+", it should not contain special characters or numbers.\n");
                                validName=false;
                                break;
                            }
                        else
                        {
                            validName=true;
                        }
                    }
                
                }
            while(!validName) //if its not a valid name, allow the user to key in again
            {
               System.out.print("Please enter the name again for Record "+(i+1)+'.');
               System.out.print("\nName: ");
               name[i]= sc.nextLine();
               break;
            }
            }while(!validName);
        }
        
    }
    public static void ICValidation(String[] identityCardNo) //IC from file validation
    {
        boolean validIC;
        Pattern p = Pattern.compile("\\d{6}-\\d{2}-\\d{4}"); //patern for ic, xxxxxx-xx-xxxx
        for(int i=0;i<identityCardNo.length;i++)
        {
            do
            {
            //validation for identityCardNo
            if (!p.matcher(identityCardNo[i]).matches())
            {
               System.out.print("Invalid IC for Record "+(i+1)+", please follow the format 'xxxxxx-xx-xxxx' and enter the IC again for Record "+(i+1)+'.');
               validIC=false;
            }
            else //validate correct birth date
            {   
                int date = Integer.parseInt(identityCardNo[i].substring(4,6));
                int month= Integer.parseInt(identityCardNo[i].substring(2,4));
                if ((date>31 || date<1)||(month<1 || month>12))
                {
                    System.out.print("Invalid IC for Record "+(i+1)+", six leading digits is incorrect, please enter the IC again for Record "+(i+1)+'.');
                    validIC=false;
                }   
                else
                {
                    validIC=true;
                }
            }
            while(!validIC) //if its not a valid IC, allow the user to key in again
            {
               System.out.print("\nIdentity card number: ");
               identityCardNo[i]=sc.nextLine();
               break;
            }
            }while(!validIC);
        }
        
    }
    public static void bloodTypeValidation(String[] bloodType) //bloodtype from file validation
    {
        boolean validBloodType;
        for(int i=0;i<bloodType.length;i++)
        {
            //validation of blood type
            do
            {
            if(!bloodType[i].equals("A+") && !bloodType[i].equals("A-")&& 
                !bloodType[i].equals("B+")&& !bloodType[i].equals("B-")&& 
                !bloodType[i].equals("O+")&& !bloodType[i].equals("O-")&& 
                !bloodType[i].equals("AB+")&& !bloodType[i].equals("AB-"))
            {
                validBloodType=false;
                System.out.print("Invalid blood type for Record "+(i+1)+"."+"\nPlease enter the blood type again for record "+(i+1)+'.');
                System.out.print("\nBlood type(A+,A-,B+,B-,O+,O-,AB+,AB-): ");
                bloodType[i]=sc.nextLine(); //if its not a valid blood type, allow the user to key in again
            }
            else
            {
                validBloodType=true;
            }
            }while(!validBloodType);
        }
    }
    
    public static void keyInHeight (double[] height, String[] name) //key in height and validation
    {
        boolean validHeight, heightNumeric=true;
        for (int i=0;i<height.length;i++)
        {
           do
           {
            System.out.print("Please enter the height for Patient "+(i+1)+", "+name[i]+".");
            System.out.print("\nHeight(in meter): ");
            String s_height = sc.nextLine();
            try //check if the height input by administrator is number
            {
              height[i] = Double.parseDouble(s_height);  //convert string to numeric type
            }
            catch (NumberFormatException e) //indicate the string is not in numeric format
            {
              heightNumeric=false;
            }
            if(heightNumeric) //validate true height value which should not be negative or zero or higher than 3m.
            {
                if (height[i]>0 && height[i]<=3)
                {
                    validHeight=true;
                }
                else
                {
                    System.out.print("Invalid height, it should not be negative value or higher than 3m.\nPlease input again.\n");
                    validHeight=false;
                }
            }
            else
            {
                System.out.print("Invalid height, please enter a valid height which is a number.\n");
                validHeight=false;
                heightNumeric=true; //reset heightNumeric to true for try catch
            }
            }while(!validHeight);
        }
    }
    public static void keyInWeight (double[] weight, String[] name) //key in weight and validation
    {
        boolean validWeight, weightNumeric=true;
        for (int i=0;i<weight.length;i++)
        {
            do
            {
        System.out.print("Please enter the weight for Patient "+(i+1)+", "+name[i]+".");
        System.out.print("\nWeight(in kilogram): ");
        String s_weight = sc.nextLine();
        try //check if the weight input by administrator is number
        {
          weight[i] = Double.parseDouble(s_weight);  //convert string to numeric type
        }
        catch (NumberFormatException e) //indicate the string is not in numeric format
        {
          weightNumeric=false;
        }
        if(weightNumeric)
        {
            if (weight[i]>0 && weight[i]<=640 ) //validate true weight value which should not be negative or zero or heavier than 640kg.
            {   
                validWeight=true;
            }
            else
            {
                System.out.print("Invalid weight, it should not be 0 or lower than 0 or heavier than 640kg.\nPlease input again.\n");
                validWeight=false;
            }
        }    
        else
        {       
                System.out.print("Invalid weight, please enter a valid height which is a number.\n");
                validWeight=false;
                weightNumeric=true; //reset weightNumeric to true for try catch
        }
        }while(!validWeight);
        }
    }
    public static void extractDOBfromIC(String[] identityCardNo, String[] dob) //extract dob from IC
    {
        for(int i=0;i<identityCardNo.length;i++)
        {
            dob[i] =identityCardNo[i].substring(4,6)+"/"
                +identityCardNo[i].substring(2,4)+"/"
                +identityCardNo[i].substring(0,2);         //store dob in format of DD/MM/YY
        }
    }
    public static void extractGenderfromIC(String[] identityCardNo, String[] gender) //extract gender from IC
    {
        for(int i=0;i<identityCardNo.length;i++)
        {
            if(identityCardNo[i].charAt(11)%2!=0)      //last digit even number is female, odd number is male
        {
           gender[i] = "Male";
        }
        else
        {
            gender[i] = "Female";
        }
        }
    }
    public static void calculateBMI(double[] height, double[] weight,double[] bmi) //calculate BMI
    {
        for(int i=0;i<height.length;i++)
        {
            bmi[i]=weight[i]/(Math.pow(height[i],2));         //calculate bmi value
        }
    }
    public static void assignWeightStatus(double[] bmi,int f, String[] weightStatus) //assign weight status according to BMI
    {
        for(int i=0;i<f;i++)
        {
            if(bmi[i]>=30)     //assigning weight status
        {
            weightStatus[i]="Obese";
        }
        else if(bmi[i]<30 && bmi[i]>=25)
        {
            weightStatus[i]="Overweight";
        }
        else if(bmi[i]<25 && bmi[i]>=18.5)
        {
            weightStatus[i]="Healthy Weight";
        }
        else
        {
             weightStatus[i]="Underweight";
        }
        }
    }
    public static double calculateHeightMean(double[] height) //calculate the height mean for the group of patients
    {
        double sum=0.0;
        for (double h : height) 
        { 
            sum+=h;
        }
        return sum/height.length;
    }
    public static double calculateHeightSD(double[] height, double heightMean) //calculate the height standard deviation for the group of patients
    {
        double sum=0.0;
        for (double h : height) 
        { 
            sum+=Math.pow(h - heightMean, 2);
        }
        return Math.sqrt(sum/height.length);
    }
    public static double calculateWeightMean(double[] weight) //calculate the weight mean for the group of patients
    {
        double sum=0.0;
        for (double w : weight) 
        { 
            sum+=w;
        }
        return sum/weight.length;
    }
    public static double calculateWeightSD(double[] weight, double weightMean) //calculate the weight standard deviation for the group of patients
    {
        double sum=0.0;
        for (double w : weight) 
        { 
            sum+=Math.pow(w - weightMean, 2);
        }
        return Math.sqrt(sum/weight.length);
    }
    public static String findHighestPatient (double[] height, String[] name) //name for highest patients
    {
        double highest = height[0];
        String patients="";
        for (int i=0;i<height.length;i++) //comparing highest height
        { 
            if(height[i]>highest)
            {
                highest=height[i];
            }
        }
        for (int j=0;j<height.length;j++)
        {
            if (height[j]==highest)
            {
                patients+=name[j]+", "; //add together all highest patients
            }
        }
      return patients;
    }
    public static String findHeaviestPatient (double[] weight, String[] name) //name for heaviest patients
    {
        double heaviest = weight[0];
        String patients="";
        for (int i=0;i<weight.length;i++)  //comparing heaviest weight
        { 
            if(weight[i]>heaviest)
            {
                heaviest=weight[i];
            }
        }
        for (int j=0;j<weight.length;j++)
        {
            if (weight[j]==heaviest)
            {
                patients+=name[j]+", "; //add together all heaviest patients
            }
        }
      return patients;
    }
    public static void writeData(int size,String[] name, String[] identityCardNo, String[] bloodType, 
            String[] weightStatus, String[] dob, String[] gender, double[] height, double[] weight, double[] bmi,
            double heightMean, double heightSD, double weightMean, double weightSD, String highestP, String heaviestP)
            /*write record of each patient, 
        the mean and standard deviation for the group of patients, and name of the highest and the heaviest patients into a text file*/
    {
        try
        {
        PrintWriter output;
        output = new PrintWriter("patientRecord.txt"); //output file name
        for(int j=0;j<size;j++)
        {
        //write patient's information
        output.println("\t\t\tRecord "+(j+1));
        output.println("=======================================================");    
        output.println("Name: \t\t\t      "+name[j]);
        output.println("Identity card number:          "+identityCardNo[j]);
        output.printf("Height: \t\t      %.2f m",height[j]);
        output.printf("\nWeight: \t\t      %.1f kg",weight[j]);
        output.println("\nBlood type: \t\t      "+bloodType[j]);
        output.println("Date of birth[DD/MM/YY]: "+dob[j]);
        output.println("Gender: \t\t      "+gender[j]);
        output.printf("BMI: \t\t\t      %.1f",bmi[j]);
        output.println("\nWeight status: \t\t      "+weightStatus[j]);
        output.println("=======================================================\n");
        }
        //write rhe mean and standard deviation for the group of patients, name of the highest and the heaviest patients 
        output.printf("\nMean of height: \t\t%.4f",heightMean);
        output.printf("\nStandard deviation of height: \t%.4f",heightSD);
        output.printf("\nMean of weight: \t\t%.4f",weightMean);
        output.printf("\nStandard deviation of weight: \t%.4f",weightSD);
        output.println("\nName of the highest patients: \t"+highestP.substring(0,(highestP.length()-2))); //-2 to delete ", " 
        output.println("Name of the heaviest patients: "+heaviestP.substring(0,(heaviestP.length()-2))); // -2 to delete ", "
        output.close();//close link
    }
            
    catch (Exception e)  
    {
        //NullPointerException, InputMismatchException, FileNotFoundException
        System.out.println("Something wrong");
    }
}
}
