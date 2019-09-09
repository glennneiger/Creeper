/*CREEPER
 *CREATED BY CONNOR MEADS
 *
 * PROGRAM FLOWS AS FOLLOWS:
 * MAIN.JAVA
 * --------------
 * static void main() //no description
 * public void start() //launches the screen where the user logs in with a set username & password or can create a new account
 * private void makeAccountScreen() //a window to add a user creating folders & .txt files to write out to
 * private void createDirectories(String l_firstName, String l_lastName, String l_username, String l_password) //backend functionality to make directories for a new user
 * private void mainScreen() //the main screen of the program where the user looks up or adds contacts
 * private Person searchPersonWindow() //a screen to search through the current list of contacts
 * private Person addPersonWindow() //a screen to add a person to contacts.  Also writes it out to the file
 * private void popUpErrorWindow(String l_error) //anytime there's a try{...}catch{...}, this function is accessed to make a pop up displaying the error
 */

package com.company;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main extends Application
{
    private ObservableList<Person> contacts = FXCollections.observableArrayList();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private User currentUser = new User();
    private Text helpText = new Text();
    private boolean showingMore = false;
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage tempStage)
    {
        //TEST
        test();
        //ENDTEST
        tempStage.setTitle("Creeper");tempStage.setWidth(800);tempStage.setHeight(400);tempStage.setResizable(false);
        Pane tempPane = new Pane();tempPane.setStyle("-fx-background-color: white");

        try //set the icon image
        {
            tempStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            popUpErrorWindow("ERROR: " + e + " (not able to load icon from internet)");
        }

        //Text Objects in the log in screen
        Text logInWelcomeText = new Text("Log In");logInWelcomeText.setX((tempStage.getWidth() / 2) - logInWelcomeText.getLayoutBounds().getWidth());logInWelcomeText.setY(tempStage.getHeight() / 4);logInWelcomeText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 26));
        Text noAccountFlavorText = new Text("Don't have an account?");noAccountFlavorText.setX(((tempStage.getWidth() / 2) - noAccountFlavorText.getLayoutBounds().getWidth() / 2) - 25);noAccountFlavorText.setY(tempStage.getHeight() - 70);noAccountFlavorText.setFont(Font.font("Times New Roman", 13));
        this.helpText.setY((tempStage.getHeight() / 3) + 160);this.helpText.setX((tempStage.getWidth() / 2) - (this.helpText.getLayoutBounds().getWidth() / 2));
        Hyperlink makeAccountText = new Hyperlink("Click Here");makeAccountText.setLayoutX(noAccountFlavorText.getX() + noAccountFlavorText.getLayoutBounds().getWidth());makeAccountText.setLayoutY(noAccountFlavorText.getY() - noAccountFlavorText.getLayoutBounds().getHeight() - 2);makeAccountText.setFont(Font.font("Times New Roman", FontWeight.THIN, 13));
        makeAccountText.setOnAction(action ->
        {
            makeAccountScreen();
            tempStage.close();
        });

        //TextField's
        TextField userNameTextField = new TextField();userNameTextField.setPromptText("Username");userNameTextField.setLayoutX((tempStage.getWidth() / 2) - 70);userNameTextField.setLayoutY(tempStage.getHeight() / 3);
        PasswordField passwordTextField = new PasswordField();passwordTextField.setPromptText("Password");passwordTextField.setLayoutX((tempStage.getWidth() / 2) - 70);passwordTextField.setLayoutY(userNameTextField.getLayoutY() + 50);

        //Buttons in log in screen
        Button verifyButton = new Button("Verify");verifyButton.setMinWidth(70);verifyButton.setMinHeight(30);verifyButton.setDefaultButton(true);verifyButton.setLayoutX((tempStage.getWidth() / 2) - 30);verifyButton.setLayoutY(passwordTextField.getLayoutY() + 50);

        //Button Functionality
        //Verify button
        verifyButton.setOnAction(action ->
        {
            if(verifyAccount(userNameTextField.getText(), passwordTextField.getText())) //successful login.  Launches the main screen of the program
            {
                tempStage.close();
                mainScreen();
            }
            else //username or password was incorrect.  displays helpful message in red
            {
                this.helpText.setText("Username or Password incorrect");helpText.setX((tempStage.getWidth() / 2) - (helpText.getLayoutBounds().getWidth() / 2));helpText.setFill(Color.RED);
            }
        });

        //This checks to see if there is a file already created.  Depending on the answer, it adjusts the helpText accordingly
        File checkFolder = new File("C:\\Creeper");
        if(!checkFolder.exists())
        {
            this.helpText.setText("It seems like this is your first time here.  Click the link below to get started!");helpText.setX((tempStage.getWidth() / 2) - (helpText.getLayoutBounds().getWidth() / 2));
        }
        else
        {
            this.helpText.setText("Welcome Back! Log in above or create a new account below!");helpText.setX((tempStage.getWidth() / 2) - (helpText.getLayoutBounds().getWidth() / 2));
        }

        tempPane.getChildren().addAll(logInWelcomeText, helpText, userNameTextField, passwordTextField, verifyButton, noAccountFlavorText, makeAccountText);
        Scene tempScene = new Scene(tempPane);
        tempStage.setScene(tempScene);
        tempStage.show();
    }

    public void test()
    {
        //stage
        Stage testStage = new Stage();testStage.setTitle("Creeper - TEST");testStage.setWidth(900);testStage.setHeight(650);

        //panes
        BorderPane root = new BorderPane();
        BorderPane topPane = new BorderPane();
        BorderPane bottomPane = new BorderPane();
        ScrollPane sp = new ScrollPane();

        sp.setContent(bottomPane);
        sp.setPannable(true);

        try //set the icon image
        {
            testStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            popUpErrorWindow("ERROR: " + e);
        }

        //make nodes for top pane
        Button button1 = new Button("Button 1 - Top Left");button1.setLayoutX(testStage.getWidth() - 890);button1.setLayoutY(testStage.getWidth() - 890);topPane.getChildren().add(button1);
        Button button2 = new Button("Button 2 - Left");
        Button button4 = new Button("Button 4 - Right");
        Button button5 = new Button("Button 5 - Right");

        Text text1 = new Text("Button 1 Flavor Text");text1.setX(button1.getLayoutX() + 50);text1.setY(button1.getTranslateY());
        Text text2 = new Text("Button 2 Flavor Text");text2.setX(button2.getTranslateX() + 50);text2.setY(button2.getTranslateY());

        root.setTop(topPane);
        root.setBottom(bottomPane);
        Scene scene = new Scene(root);
        testStage.setScene(scene);
        testStage.showAndWait();
    }

    private void makeAccountScreen() //opens a window allowing user to create an account with a fresh set of contacts
    {
        //make and launch new stage
        Stage tempStage = new Stage();tempStage.setWidth(500);tempStage.setHeight(300);tempStage.initModality(Modality.APPLICATION_MODAL);tempStage.setTitle("Account Creation");//tempStage.setResizable(false);
        Pane tempPane = new Pane();tempPane.setStyle("-fx-background-color: white");
        String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"; //a regex to check for valid emails
        User newUser = new User(); //creating a new User to work with

        try //set the icon image
        {
            tempStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            popUpErrorWindow("ERROR: " + e);
        }

        //Text Objects
        Text informationalText = new Text("A Few Things About You...");informationalText.setX(tempStage.getWidth() - (tempStage.getWidth() * 0.98));informationalText.setY(tempStage.getHeight() - 280);informationalText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        Text helpText = new Text();helpText.setX(informationalText.getX());helpText.setY(informationalText.getY() + 20);helpText.setFont(Font.font("Verdana", 12));helpText.setVisible(false);helpText.setFill(Color.RED);
        Text textPromptUL= new Text("First Name");textPromptUL.setX(tempStage.getWidth() - (tempStage.getWidth() * 0.98));textPromptUL.setY(informationalText.getY() + 60);textPromptUL.setFont(Font.font("Times New Roman", 14));
        Text textPromptUR = new Text("Last Name");textPromptUR.setX(textPromptUL.getX() + 170);textPromptUR.setY(textPromptUL.getY());textPromptUR.setFont(Font.font("Times New Roman", 14));
        Text textPromptLL = new Text("Email Address");textPromptLL.setX(textPromptUL.getX());textPromptLL.setY(textPromptUL.getY() + 60);textPromptLL.setFont(Font.font("Times New Roman", 14));
        Text textPromptLR = new Text("Measurement Preference");textPromptLR.setX(textPromptUR.getX());textPromptLR.setY(textPromptLL.getY());textPromptLR.setFont(Font.font("Times New Roman",14));

        //TextField Objects
        TextField textFieldUL = new TextField();textFieldUL.setLayoutX(textPromptUL.getX());textFieldUL.setLayoutY(textPromptUL.getY() + 10);textFieldUL.setVisible(true);
        TextField textFieldUR = new TextField();textFieldUR.setLayoutX(textPromptUR.getX());textFieldUR.setLayoutY(textFieldUL.getLayoutY());textFieldUR.setVisible(true);
        TextField textFieldLL = new TextField();textFieldLL.setLayoutX(textPromptLL.getX());textFieldLL.setLayoutY(textPromptLL.getY() + 10);textFieldLL.setVisible(true);

        //passwordField Object(s)
        PasswordField passwordTextField = new PasswordField();passwordTextField.setLayoutX(textFieldUR.getLayoutX());passwordTextField.setLayoutY(textFieldUR.getLayoutY());passwordTextField.setPromptText("Password");passwordTextField.setVisible(false);

        //Radio Buttons
        ToggleGroup preference = new ToggleGroup();
        RadioButton metricPreferenceRB = new RadioButton("Metric");metricPreferenceRB.setLayoutX(textPromptLR.getX());metricPreferenceRB.setLayoutY(textPromptLR.getY() + 15);metricPreferenceRB.setSelected(true);
        RadioButton imperialPreferenceRB = new RadioButton("Imperial");imperialPreferenceRB.setLayoutX(metricPreferenceRB.getLayoutX() + 75);imperialPreferenceRB.setLayoutY(metricPreferenceRB.getLayoutY());

        metricPreferenceRB.setToggleGroup(preference);
        imperialPreferenceRB.setToggleGroup(preference);

        //Images
        Image arrowImage = new Image("https://cdn3.iconfinder.com/data/icons/ui-elements-light/100/UI_Light_chevron_right-512.png");
        ImageView arrowImageView = new ImageView(arrowImage);arrowImageView.setFitHeight(100);arrowImageView.setFitWidth(40);

        //Buttons
        Button continueButton = new Button("", arrowImageView);continueButton.setPrefSize(40, 262.3);continueButton.setLayoutX(tempStage.getWidth() - 70);continueButton.setStyle("-fx-background-color: white;");continueButton.setStyle("-fx-border-color: black;");
        Button backButton = new Button("<");backButton.setLayoutX(informationalText.getX());backButton.setLayoutY(tempStage.getHeight() - 115);backButton.setPrefSize(35, 20);backButton.setVisible(false);
        Button finishButton = new Button("Finish");finishButton.setLayoutX(informationalText.getX());finishButton.setLayoutY(tempStage.getHeight() - 80);finishButton.setPrefSize(70, 30);finishButton.setVisible(false);

        //button action
        continueButton.setOnAction(action ->
        {
            /* GUIDE TO THE continueButton
             * Step 1: Check to make sure the text fields aren't blank & the email entered looks like a valid email
             * Step 2: Save the information to the correct variables
             * Step 3: Adjust the visibilities as needed
             */
            //STEP 1
            if(!(textFieldUL.getText().length() == 0) && !(textFieldUR.getText().length() == 0) && !(textFieldLL.getText().length() == 0) && (textFieldLL.getText().matches(emailRegex)))
            {
                //STEP 2
                newUser.setFirstName(textFieldUL.getText());
                newUser.setLastName(textFieldUR.getText());
                newUser.setEmail(textFieldLL.getText());

                if(metricPreferenceRB.isSelected())
                {
                    newUser.setIsMetricUser(true);
                }
                else
                {
                    newUser.setIsMetricUser(false);
                }

                //STEP 3
                //Visibility
                helpText.setVisible(false);
                continueButton.setVisible(false);
                backButton.setVisible(true);
                finishButton.setVisible(true);
                passwordTextField.setVisible(true);
                textFieldLL.setVisible(false);
                textFieldUR.setVisible(false);
                textPromptLL.setVisible(false);
                textPromptLR.setVisible(false);
                metricPreferenceRB.setVisible(false);
                imperialPreferenceRB.setVisible(false);

                //readjust prompts
                informationalText.setText("A Few Things About You...");
                textPromptUL.setText("Username");
                textPromptUR.setText("Password");
                textFieldUL.setText(newUser.getUsername());
            }
            else
            {
                helpText.setVisible(true);
                helpText.setText("Make sure the first & last name text fields aren't blank\nand you have a valid email address before continuing!");
            }
        });
        backButton.setOnAction(action ->
        {
            /* GUIDE TO backButton
             * Step 1: Adjust visibility to nodes
             * Step 2: clear any progress on username & password nodes & readjust prompts
             */
            //Step 1
            passwordTextField.setVisible(false);
            continueButton.setVisible(true);
            backButton.setVisible(false);
            finishButton.setVisible(false);
            textFieldLL.setVisible(true);
            textFieldUR.setVisible(true);
            textPromptLL.setVisible(true);
            textPromptLR.setVisible(true);
            metricPreferenceRB.setVisible(true);
            imperialPreferenceRB.setVisible(true);

            //Step 2
            newUser.setUsername(textFieldUL.getText());
            passwordTextField.clear();
            informationalText.setText("Account Information");
            textPromptUL.setText("First Name");
            textPromptUR.setText("Last Name");
            textFieldUL.setText(newUser.getFirstName());
        });
        finishButton.setOnAction(action ->
        {
            /* GUIDE TO finishButton
             * Step 1: Save everything to the newUser variable and pass it along to the createDirectories function
             * Step 2: Close the stage & log you in automatically
             */

            //Step 1
            newUser.setUsername(textFieldUL.getText());
            newUser.setEncryptedPassword(newUser.hash(passwordTextField.getText()));
            createDirectories(newUser);

            //Step 2
            if(verifyAccount(newUser.getUsername(), passwordTextField.getText()))
            {
                mainScreen();
                tempStage.close();
            }
            else
            {
                popUpErrorWindow("Go get Connor,\ncan't validate a brand new user");
            }
        });

        tempPane.getChildren().addAll(informationalText, helpText, textPromptUL, textPromptUR, textPromptLL, textPromptLR, textFieldUL, textFieldUR, textFieldLL, passwordTextField, metricPreferenceRB, imperialPreferenceRB, finishButton, backButton, continueButton);
        Scene tempScene = new Scene(tempPane);
        tempStage.setScene(tempScene);
        tempStage.show();
    }

    private void createDirectories(User l_newUser) //makes folders & .txt folders for new user accounts as well as adds that user to the users ObservableArrayList
    {
        //local variables
        boolean noErrors = true;
        String errorMsg = "";

        //Make folders
        File folders = new File("C:\\Creeper\\" + l_newUser.getUsername());
        try
        {
            if(!folders.exists())
            {
                if(!folders.mkdirs())
                {
                    noErrors = false;
                    errorMsg = errorMsg + "Failed to make Directory: 'Creeper'\n";
                }
            }
            else
            {
                noErrors = false;
                errorMsg = errorMsg + "Directory already exists\n";
            }
        }catch(Exception e)
        {
            popUpErrorWindow("ERROR: " + e.toString());
        }

        //make .txt files
        File accountInfoTextFile = new File("C:\\Creeper\\" + l_newUser.getUsername() + "\\accountInfo.txt");
        File contactsInfoTextFile = new File("C:\\Creeper\\" + l_newUser.getUsername() + "\\contacts.txt");

        try
        {
            if(!(accountInfoTextFile.createNewFile() && contactsInfoTextFile.createNewFile()))
            {
                noErrors = false;
                errorMsg = errorMsg + "Failed to create text files\n";
            }
        }catch (Exception e)
        {
            popUpErrorWindow("ERROR: " + e.toString());
        }

        //write out the account info to accountInfo.txt
        try
        {
            PrintWriter writer = new PrintWriter("C:\\Creeper\\" + l_newUser.getUsername() + "\\accountInfo.txt");
            writer.println(l_newUser.getFirstName() + ">~>" + l_newUser.getLastName()); //Write out to the .txt file "John Doe"
            writer.println(l_newUser.getUsername()); //on a new line, write out the username
            writer.println(l_newUser.getEncryptedPassword()); //on a new line, write out the encrypted password
            writer.println("Unassigned>~>Friends>~>Family>~>Co-Workers");
            if(l_newUser.getIsMetricUser())
            {
                writer.println("T");
            }
            else
            {
                writer.println("F");
            }
            writer.close();
            l_newUser.addCategory("Unassigned");
            l_newUser.addCategory("Friends");
            l_newUser.addCategory("Family");
            l_newUser.addCategory("Co-Workers");
        }catch (Exception e)
        {
            popUpErrorWindow("Error: " + e.toString());
        }

        if(noErrors)
        {
            helpText.setText("Account Creation Successful!");helpText.setX(400 - (helpText.getLayoutBounds().getWidth() / 2));helpText.setFill(Color.GREEN);
        }
        else
        {
            helpText.setText(errorMsg);
            helpText.setFill(Color.RED);
        }
    }

    private boolean verifyAccount(String l_username, String l_passwordAttempt) //verifies username & password as well as reads in all account information and contacts information
    {
        //local variables
        User newUser;//a User object to create a new User
        Person newPerson;//a Person object to represent the current Person being read in and saved to the global contacts ObservableList
        File readFile = new File("C:\\Creeper\\" + l_username); //File data type to store the file path to the pertinent information
        BufferedReader br;//the BufferedReader used to read in the .txt files
        ArrayList<String> fileContents = new ArrayList<>(); //to store the fileContents of both accountInfo.txt & contacts.txt at different times
        String[] splitFullName;//get the first & last name by splitting the full Name up using a white space
        String firstName, lastName, fullName; //Strings to store the individual first, last, and full name of that found in accountInfo.txt
        String username, encryptedPasswordReadIn, encryptedPasswordInput; //Strings to store the username and password read in as found in accountInfo.txt.  Also contains the encryptedPassword that the user types in and is verified through encryption
        String categoryReadIn; //The concatenated String of categories
        String[] userCategories; //Array of Strings representing all the categories the User has previously entered
        String measurementData;
        String str;//a placeholder string to read in the files and save them to the ArrayList 'fileContents'

        try
        {
            if(readFile.exists())
            {
                readFile = new File("C:\\Creeper\\" + l_username + "\\accountInfo.txt");
                br = new BufferedReader(new FileReader(readFile));

                while((str = br.readLine()) != null) //save the file contents to a ArrayList
                {
                    fileContents.add(str);
                }

                fullName = fileContents.get(0);splitFullName = fullName.split(">~>");firstName = splitFullName[0];lastName = splitFullName[1]; //split the name String
                username = fileContents.get(1);
                encryptedPasswordReadIn = fileContents.get(2);
                categoryReadIn = fileContents.get(3);
                measurementData = fileContents.get(4);
                userCategories = categoryReadIn.split(">~>", -1);
                newUser = new User();
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setUsername(username);
                encryptedPasswordInput = newUser.hash(l_passwordAttempt);

                if(measurementData.equals("T")) //User prefers the metric system
                {
                    newUser.setIsMetricUser(true);
                }
                else //tsk tsk
                {
                    newUser.setIsMetricUser(false);
                }

                for(String st : userCategories)
                {
                    newUser.addCategory(st);
                }

                this.users.add(newUser); //add the user to the global ArrayList
                this.currentUser = newUser; //make the current user equal to who they logged in to

                br.close();

                readFile = new File("C:\\Creeper\\" + l_username + "\\contacts.txt");
                br = new BufferedReader(new FileReader(readFile));

                fileContents.clear();
                while((str = br.readLine()) != null) //read in the contents of the file & add them to an ArrayList<String> where each item in the list represents a Person object
                {
                    fileContents.add(str);
                }

                for(int i = 0; i < fileContents.size(); i++) //goes through each line of contacts.txt and splits them apart via regex
                {
                    //loop variables
                    newPerson = new Person();
                    String[] personInfo;
                    String[] nameAndDoB;
                    String[] addressArray;
                    String[] addressInfo;
                    String[] phoneNumberArray;
                    String[] phoneNumberInfo;
                    String[] emailArray;
                    String[] emailInfo;
                    String[] categories;
                    LocalDate tempDoB;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    //split the current line into 5 parts to be further regex'd
                    personInfo = fileContents.get(i).split("<<!>>", -1);

                    //split & save the contacts name & date of birth
                    nameAndDoB = personInfo[0].split(">~>", -1);
                    newPerson.setLastName(nameAndDoB[0]);
                    newPerson.setFirstName(nameAndDoB[1]);
                    newPerson.setMiddleName(nameAndDoB[2]);
                    tempDoB = LocalDate.parse(nameAndDoB[3], formatter);
                    newPerson.setDateOfBirth(tempDoB);
                    newPerson.setSex(nameAndDoB[4]);
                    newPerson.setWeight(nameAndDoB[5]);
                    newPerson.setHeight(nameAndDoB[6]);
                    newPerson.setEyeColor(nameAndDoB[7]);
                    newPerson.setHairColor(nameAndDoB[8]);
                    newPerson.setComment(nameAndDoB[9].replaceAll("<'r'>", "\n")); //replaces the placeholder for a carriage return, with a carriage return

                    //split & save the contacts addresses
                    addressArray = personInfo[1].split("/&/", -1);

                    for(int j = 0; j < addressArray.length; j++)
                    {
                        addressInfo = addressArray[j].split(">~>", -1);
                        PersonAddress newPersonAddress = new PersonAddress(addressInfo[0], addressInfo[1], addressInfo[2], addressInfo[3], addressInfo[4]);

                        newPerson.addAddress(newPersonAddress);
                    }

                    //split & save the contacts Phone Numbers
                    phoneNumberArray = personInfo[2].split("/&/", -1);

                    for(int j = 0; j < phoneNumberArray.length; j++)
                    {
                        phoneNumberInfo = phoneNumberArray[j].split(">~>", -1);
                        PersonPhoneNumber newPhoneNumber = new PersonPhoneNumber(phoneNumberInfo[0], phoneNumberInfo[1]);

                        newPerson.addPhoneNumber(newPhoneNumber);
                    }

                    //split & save the contacts emails
                    emailArray = personInfo[3].split("/&/", -1);

                    for(int j = 0; j < emailArray.length; j++)
                    {
                        emailInfo = emailArray[j].split(">~>", -1);
                        PersonEmail newEmail = new PersonEmail(emailInfo[0], emailInfo[1]);

                        newPerson.addEmail(newEmail);
                    }

                    //split & save the categories
                    categories = personInfo[4].split(">~>", -1);

                    for(int j = 0; j < categories.length; j++)
                    {
                        newPerson.addCategory(categories[j]);
                    }

                    contacts.add(newPerson);
                }

                br.close();
                return encryptedPasswordInput.equals(encryptedPasswordReadIn); //final return if password & username match
            }
            else
            {
                return false;
            }
        } catch (Exception e)
        {
            popUpErrorWindow("ERROR: " + e);
            return false;
        }
    }

    private void mainScreen() //the home page
    {
        //local variables
        ObservableList<Person> recentlySearchedPeople = FXCollections.observableArrayList();
        ArrayList<Text> addresses = new ArrayList<>();
        ArrayList<Text> phoneNumbers = new ArrayList<>();
        ArrayList<Text> emails = new ArrayList<>();
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        final int xIncrement = 10;
        final int yIncrement = 10;

        //set all the defaults for the stage
        Stage primaryStage = new Stage();primaryStage.setTitle("Creeper");primaryStage.setWidth(900);primaryStage.setHeight(650);//primaryStage.setResizable(false);

        //panes
        BorderPane root = new BorderPane();
        Pane topPane = new Pane();
        Pane bottomPane = new Pane();
        ScrollPane sp = new ScrollPane();

        try //set the icon image
        {
            primaryStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            popUpErrorWindow("ERROR: " + e);
        }

        //declare nodes on screen
        //TOP PANE NODES
        //Images
        Image contactPhoto = new Image("https://sdmg.com/wp-content/uploads/2017/04/picture-not-available.jpg?x67906");
        ImageView imageView = new ImageView(contactPhoto);imageView.setX(primaryStage.getWidth() - 890);imageView.setY(primaryStage.getWidth() - 890);imageView.setFitHeight(170);imageView.setFitWidth(140);imageView.setPreserveRatio(true);

        //Text
        Text nameText = new Text("[Name]");nameText.setX(imageView.getX() + 150);nameText.setY(imageView.getY() + 10);nameText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 22));
        Text DoBText = new Text("[DoB]");DoBText.setX(imageView.getX() + 150);DoBText.setY(primaryStage.getHeight() - 600);DoBText.setFont(Font.font("Times New Roman", 14));
        Text addressText = new Text("[Address]");addressText.setX(imageView.getX() + 150);addressText.setY(primaryStage.getHeight() - 550);addressText.setFont(Font.font("Times New Roman", 18));
        Text phoneNumberText = new Text("[Phone #]");phoneNumberText.setX(imageView.getX() + 150);phoneNumberText.setY(primaryStage.getHeight() - 470);phoneNumberText.setFont(Font.font("Times New Roman", 18));
        Hyperlink moreDetailsText = new Hyperlink("More Details  v");moreDetailsText.setLayoutX(primaryStage.getWidth() - 890);moreDetailsText.setLayoutY(primaryStage.getHeight() - 445);moreDetailsText.setFont(Font.font("Times New Roman", FontWeight.THIN, 13));moreDetailsText.setBorder(Border.EMPTY);

        //buttons
        Button addPersonButton = new Button("Add Person");addPersonButton.setMinHeight(40);addPersonButton.setMinWidth(100);addPersonButton.setLayoutX(primaryStage.getWidth() - 130);addPersonButton.setLayoutY(primaryStage.getHeight() - 630);
        Button searchExistingButton = new Button("Search Existing");searchExistingButton.setMinHeight(40);searchExistingButton.setMinWidth(100);searchExistingButton.setLayoutX(primaryStage.getWidth() - 130);searchExistingButton.setLayoutY(primaryStage.getHeight() - 580);searchExistingButton.setMaxWidth(100);searchExistingButton.setStyle("-fx-font-size:11");

        //Add nodes of top pane to the top pane
        topPane.getChildren().addAll(nameText, DoBText, addressText, phoneNumberText, moreDetailsText, addPersonButton, searchExistingButton, imageView);

        //BOTTOM PANE NODES
        //listView
        ListView<Person> recentlySearchedListView = new ListView<>();recentlySearchedListView.setPrefSize(170, 280);recentlySearchedListView.setLayoutX(primaryStage.getWidth() - 200);recentlySearchedListView.setLayoutY(primaryStage.getHeight() - 640);

        //Text
        Text addressTitleText = new Text("Addresses");addressTitleText.setX(imageView.getX());addressTitleText.setY(recentlySearchedListView.getLayoutY() + yIncrement * 2);addressTitleText.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 22));addressTitleText.setVisible(false);
        Text primaryAddressSubTitleText = new Text("Primary Address");primaryAddressSubTitleText.setX(addressTitleText.getX() + xIncrement);primaryAddressSubTitleText.setY(addressTitleText.getY() + yIncrement * 2);primaryAddressSubTitleText.setUnderline(true);primaryAddressSubTitleText.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 16));primaryAddressSubTitleText.setVisible(false);
        Text secondaryAddressSubTitleText = new Text("Secondary Address");secondaryAddressSubTitleText.setX(primaryAddressSubTitleText.getX());secondaryAddressSubTitleText.setY(primaryAddressSubTitleText.getY() + yIncrement * 6);secondaryAddressSubTitleText.setUnderline(true);secondaryAddressSubTitleText.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 16));secondaryAddressSubTitleText.setVisible(false);

        Text phoneNumberTitleText = new Text("Phone Numbers");phoneNumberTitleText.setX(addressTitleText.getX());phoneNumberTitleText.setY(secondaryAddressSubTitleText.getY() + yIncrement * 8);phoneNumberTitleText.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 22));phoneNumberTitleText.setVisible(false);
        Text primaryPhoneNumberSubTitleText = new Text("Primary Phone Number");primaryPhoneNumberSubTitleText.setX(primaryAddressSubTitleText.getX());primaryPhoneNumberSubTitleText.setY(phoneNumberTitleText.getY() + yIncrement * 2);primaryPhoneNumberSubTitleText.setUnderline(true);primaryPhoneNumberSubTitleText.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 16));primaryPhoneNumberSubTitleText.setVisible(false);
        Text secondaryPhoneNumberSubTitleText = new Text("Secondary Phone Numbers");secondaryPhoneNumberSubTitleText.setX(primaryPhoneNumberSubTitleText.getX());secondaryPhoneNumberSubTitleText.setY(primaryPhoneNumberSubTitleText.getY() + yIncrement * 6);secondaryPhoneNumberSubTitleText.setUnderline(true);secondaryPhoneNumberSubTitleText.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 16));secondaryPhoneNumberSubTitleText.setVisible(false);

        Text emailTitleText = new Text("Emails");emailTitleText.setX(phoneNumberTitleText.getX());emailTitleText.setY(secondaryPhoneNumberSubTitleText.getY() + yIncrement * 8);emailTitleText.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 22));emailTitleText.setVisible(false);
        Text primaryEmailSubTitleText = new Text("Primary Email");primaryEmailSubTitleText.setX(primaryAddressSubTitleText.getX());primaryEmailSubTitleText.setY(emailTitleText.getY() + yIncrement * 2);primaryEmailSubTitleText.setUnderline(true);primaryEmailSubTitleText.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 16));primaryEmailSubTitleText.setVisible(false);
        Text secondaryEmailSubTitleText = new Text("Secondary Emails");secondaryEmailSubTitleText.setX(primaryEmailSubTitleText.getX());secondaryEmailSubTitleText.setY(primaryEmailSubTitleText.getY() + yIncrement * 6);secondaryEmailSubTitleText.setUnderline(true);secondaryEmailSubTitleText.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 16));secondaryEmailSubTitleText.setVisible(false);
        //buttons
        Button exitButton = new Button("Exit");exitButton.setMinHeight(50);exitButton.setMinWidth(100);exitButton.setLayoutX(primaryStage.getWidth() - 130); exitButton.setLayoutY(primaryStage.getHeight() - 320);

        //Add nodes of bottom pane to the bottom pane
        bottomPane.getChildren().addAll(recentlySearchedListView, exitButton, addressTitleText, primaryAddressSubTitleText, secondaryAddressSubTitleText, phoneNumberTitleText, primaryPhoneNumberSubTitleText, secondaryPhoneNumberSubTitleText, emailTitleText, primaryEmailSubTitleText, secondaryEmailSubTitleText);

        //node Functions
        //Hyperlink
        moreDetailsText.setOnAction(action ->
        {
            if(showingMore) //show less
            {
                showingMore = false;
                moreDetailsText.setText("More Details  v");

                //Change the visibility of nodes
                //upper pane
                addressText.setVisible(true);
                phoneNumberText.setVisible(true);

                //lower pane
                addressTitleText.setVisible(false);
                primaryAddressSubTitleText.setVisible(false);
                secondaryAddressSubTitleText.setVisible(false);
                for(Text address : addresses)
                {
                    address.setVisible(false);
                }

                phoneNumberTitleText.setVisible(false);
                primaryPhoneNumberSubTitleText.setVisible(false);
                secondaryPhoneNumberSubTitleText.setVisible(false);
                for(Text phoneNumber : phoneNumbers)
                {
                    phoneNumber.setVisible(false);
                }

                emailTitleText.setVisible(false);
                primaryEmailSubTitleText.setVisible(false);
                secondaryEmailSubTitleText.setVisible(false);
                for(Text email : emails)
                {
                    email.setVisible(false);
                }
            }
            else //show more
            {
                showingMore = true;
                moreDetailsText.setText("Less Details  ^");

                //change the visibility of nodes
                //upper pane
                addressText.setVisible(false);
                phoneNumberText.setVisible(false);

                //lower pane
                addressTitleText.setVisible(true);
                primaryAddressSubTitleText.setVisible(true);
                secondaryAddressSubTitleText.setVisible(true);
                for(Text address : addresses)
                {
                    address.setVisible(true);
                }

                phoneNumberTitleText.setVisible(true);
                primaryPhoneNumberSubTitleText.setVisible(true);
                secondaryPhoneNumberSubTitleText.setVisible(true);
                for(Text phoneNumber : phoneNumbers)
                {
                    phoneNumber.setVisible(true);
                }

                emailTitleText.setVisible(true);
                primaryEmailSubTitleText.setVisible(true);
                secondaryEmailSubTitleText.setVisible(true);
                for(Text email : emails)
                {
                    email.setVisible(true);
                }
            }
        });
        //listView
        for(int i = 0; i < recentlySearchedPeople.size(); i++)
        {
            recentlySearchedListView.getItems().add(recentlySearchedPeople.get(i)); //TODO: Use your new knowledge of data structures to figure out how to put the most recent search at the top
        }
        //buttons
        exitButton.setOnAction(action ->  //TODO: Have the Exit Button scroll with the screen
        {
            System.exit(0);
        });
        bottomPane.addEventHandler(ScrollEvent.SCROLL, e ->
        {
            exitButton.setLayoutY(primaryStage.getHeight() - 320);
        });

        addPersonButton.setOnAction(action ->
        {
            contacts.add(addPersonWindow());
        });

        searchExistingButton.setOnAction(action ->
        {
            /* GUIDE TO THIS BUTTON
             * 1) Wipe the panes clear of the last contact
             * 2) Reset the positions of bottom pane nodes
             * 3) Actually search for your contacts via the method searchPersonWindow
             * 4) Set the positions and create the Text objects necessary to properly display the selected contact
            */
            //wipe info from labels & clear ArrayLists
            nameText.setText("");
            DoBText.setText("");
            addressText.setText("");
            phoneNumberText.setText("");

            for(int i = 0; i < addresses.size(); i++)
            {
                bottomPane.getChildren().remove(addresses.get(i));
            }
            addresses.clear();
            for(int i = 0; i < phoneNumbers.size(); i++)
            {
                bottomPane.getChildren().remove(phoneNumbers.get(i));
            }
            phoneNumbers.clear();
            for(int i = 0; i < emails.size(); i++)
            {
                bottomPane.getChildren().remove(emails.get(i));
            }
            phoneNumbers.clear();

            //reset positions of bottom pane nodes
            addressTitleText.setY(recentlySearchedListView.getLayoutY() + yIncrement * 2);
            primaryAddressSubTitleText.setY(addressTitleText.getY() + yIncrement * 2);
            secondaryAddressSubTitleText.setY(primaryAddressSubTitleText.getY() + yIncrement * 6);

            phoneNumberTitleText.setY(secondaryAddressSubTitleText.getY() + yIncrement * 8);
            primaryPhoneNumberSubTitleText.setY(phoneNumberTitleText.getY() + yIncrement * 2);
            secondaryPhoneNumberSubTitleText.setY(primaryPhoneNumberSubTitleText.getY() + yIncrement * 6);

            emailTitleText.setY(secondaryPhoneNumberSubTitleText.getY() + yIncrement * 8);
            primaryEmailSubTitleText.setY(emailTitleText.getY() + yIncrement * 2);
            secondaryEmailSubTitleText.setY(primaryEmailSubTitleText.getY() + yIncrement * 6);

            Person selectedPerson = searchPersonWindow();
            if(selectedPerson == null) //display error message if no contact was selected
            {
                nameText.setText("Error: No Contact was Selected...");
            }
            else
            {
                for(int i = 0; i < selectedPerson.getAddresses().size(); i++) //making the List of address Text objects
                {
                    Text newAddress = new Text(selectedPerson.getAddresses().get(i).getStreetAddress() + " " + selectedPerson.getAddresses().get(i).getCity() + ", " + selectedPerson.getAddresses().get(i).getState() + " " + selectedPerson.getAddresses().get(i).getZipcode() + "  [" + selectedPerson.getAddresses().get(i).getType() + "]");
                    newAddress.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 16));
                    if(showingMore)
                        newAddress.setVisible(true);
                    else
                        newAddress.setVisible(false);

                    if(i == 0) //Setting the first Address as the primary address
                    {
                        newAddress.setX(primaryAddressSubTitleText.getX());
                        newAddress.setY(primaryAddressSubTitleText.getY() + yIncrement * 3);
                    }
                    else //Setting the rest of the addresses as secondary addresses shown below
                    {
                        newAddress.setX(primaryAddressSubTitleText.getX());
                        newAddress.setY(secondaryAddressSubTitleText.getY() + (yIncrement * 2) + (yIncrement * 2 * i));
                    }
                    bottomPane.getChildren().add(newAddress);

                    //move nodes below this one accordingly
                    if(i >= 2)
                    {
                        phoneNumberTitleText.setY(phoneNumberTitleText.getY() + yIncrement * i);
                        primaryPhoneNumberSubTitleText.setY(primaryPhoneNumberSubTitleText.getY() + yIncrement * i);
                        secondaryPhoneNumberSubTitleText.setY(secondaryPhoneNumberSubTitleText.getY() + yIncrement * i);

                        emailTitleText.setY(emailTitleText.getY() + yIncrement * i);
                        primaryEmailSubTitleText.setY(primaryEmailSubTitleText.getY() + yIncrement * i);
                        secondaryEmailSubTitleText.setY(secondaryEmailSubTitleText.getY() + yIncrement *i);
                    }
                    addresses.add(newAddress);
                }

                for(int i = 0; i < selectedPerson.getPhoneNumbers().size(); i++) //making the List of phone number Text objects
                {
                    Text newPhoneNumber = new Text(selectedPerson.getPhoneNumbers().get(i).getPhoneNumber() + "  [" + selectedPerson.getPhoneNumbers().get(i).getType() + "]");
                    newPhoneNumber.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 16));
                    if(showingMore)
                        newPhoneNumber.setVisible(true);
                    else
                        newPhoneNumber.setVisible(false);

                    if(i ==0) //Setting first phone # as primary phone #
                    {
                        newPhoneNumber.setX(primaryPhoneNumberSubTitleText.getX());
                        newPhoneNumber.setY(primaryPhoneNumberSubTitleText.getY() + yIncrement * 3);
                    }
                    else //setting the rest of the phone numbers as secondary phone #
                    {
                        newPhoneNumber.setX(primaryPhoneNumberSubTitleText.getX());
                        newPhoneNumber.setY(secondaryPhoneNumberSubTitleText.getY() + (yIncrement * 2) + (yIncrement * 2 * i));
                    }
                    bottomPane.getChildren().add(newPhoneNumber);

                    //move nodes below this one accordingly
                    if(i >= 2)
                    {
                        emailTitleText.setY(emailTitleText.getY() + yIncrement * i);
                        primaryEmailSubTitleText.setY(primaryEmailSubTitleText.getY() + yIncrement * i);
                        secondaryEmailSubTitleText.setY(secondaryEmailSubTitleText.getY() + yIncrement *i);
                    }
                    phoneNumbers.add(newPhoneNumber);
                }

                for(int i = 0; i < selectedPerson.getEmails().size(); i++) // making a list of email Text objects
                {
                    Text newEmail = new Text(selectedPerson.getEmails().get(i).getEmailAddress() + "  [" + selectedPerson.getEmails().get(i).getType() + "]");
                    newEmail.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 16));
                    if(showingMore)
                        newEmail.setVisible(true);
                    else
                        newEmail.setVisible(false);

                    if(i ==0) //Setting first email as primary email
                    {
                        newEmail.setX(primaryEmailSubTitleText.getX());
                        newEmail.setY(primaryEmailSubTitleText.getY() + yIncrement * 3);
                    }
                    else //setting the rest of the emails as secondary emails
                    {
                        newEmail.setX(primaryEmailSubTitleText.getX());
                        newEmail.setY(secondaryEmailSubTitleText.getY() + (yIncrement * 2) + (yIncrement * 3 * i));
                    }
                    bottomPane.getChildren().add(newEmail);

                    emails.add(newEmail);
                }

                //Set the overhead text to display appropriate data
                String dateString = dtFormatter.format(selectedPerson.getDateOfBirth());
                recentlySearchedListView.getItems().add(selectedPerson);
                nameText.setText(selectedPerson.toString());
                DoBText.setText(dateString);
                if(addresses.size() > 0)
                    addressText.setText(addresses.get(0).getText());
                else
                    addressText.setText("No Address Found");
                if(phoneNumbers.size() > 0)
                    phoneNumberText.setText(phoneNumbers.get(0).getText());
                else
                    phoneNumberText.setText("No Phone Number Foudn");
            }
        });

        //Scroll Pane
        sp.setContent(bottomPane);
        sp.setPannable(true);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        //Adding the rest of the panes
        root.setTop(topPane);
        root.setCenter(sp);

        //Scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Person searchPersonWindow() //opens a window enabling the user to search through their current list of contacts
    {
        //local variables
        int listBoxIndex = 0;

        //make new stage & pane
        Stage tempStage = new Stage();tempStage.initModality(Modality.APPLICATION_MODAL);tempStage.setTitle("Search Person");tempStage.setWidth(550);tempStage.setHeight(600);tempStage.setResizable(false);
        Pane tempPane = new Pane();

        try //set the icon image
        {
            tempStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            popUpErrorWindow("ERROR: " + e);
        }

        //make all objects in searchPersonWindow
        //Text objects
        Text advancedSearch = new Text("more advanced search");advancedSearch.setX(tempStage.getWidth() - 545);advancedSearch.setY(tempStage.getHeight() - 50);

        //TextBox's
        TextField nameTextField = new TextField();nameTextField.setLayoutX(tempStage.getWidth() - 545);nameTextField.setLayoutY(tempStage.getHeight() - 580);nameTextField.setPromptText("Last, First Middle Name");
        TextField phoneNumberTextField = new TextField();phoneNumberTextField.setLayoutX(tempStage.getWidth() - 357.5);phoneNumberTextField.setLayoutY(tempStage.getHeight() - 580);phoneNumberTextField.setPromptText("Phone Number");
        TextField addressTextField = new TextField();addressTextField.setLayoutX(tempStage.getWidth() - 170);addressTextField.setLayoutY(tempStage.getHeight() - 580);addressTextField.setPromptText("Address");

        //ListBox
        ListView<Person> searchBox = new ListView<>();searchBox.setLayoutX(tempStage.getWidth() - 515);searchBox.setLayoutY(tempStage.getHeight() - 540);searchBox.setMinHeight(400);searchBox.setMinWidth(470);

        //Buttons
        Button selectButton = new Button("Select");selectButton.setLayoutX(searchBox.getLayoutX());selectButton.setLayoutY(tempStage.getHeight() - 125);selectButton.setMinHeight(40);selectButton.setMinWidth(80);
        Button cancelButton = new Button("Cancel");cancelButton.setLayoutX(tempStage.getWidth() - 125);cancelButton.setLayoutY(tempStage.getHeight() - 125);cancelButton.setMinHeight(40);cancelButton.setMinWidth(80);

        //Node Functionality
        //TextBox's
        nameTextField.setOnAction(action ->
        {
            searchBox.getItems().clear();
            String tempFirstName = "", tempMiddleName = "", tempLastName = "";
            String[] tempName = nameTextField.getText().split("\\s"); //splits the string based on the spaces
            for(int i = 0; i < tempName.length; i++) //forloop to replace all commas within name
            {
                if (tempName[i].contains(","))
                {
                    tempName[i] = tempName[i].replace(",", "");
                }
            }
            //if block to determine the order of the names
            if(tempName.length == 1)
            {
                tempFirstName = tempName[0];
            }
            else if(tempName.length == 2)
            {
                tempFirstName = tempName[0];
                tempLastName = tempName[1];
            }
            else if(tempName.length == 3)
            {
                tempLastName = tempName[0];
                tempFirstName = tempName[1];
                tempMiddleName = tempName[2];
            }
            for(int i = 0; i < this.contacts.size(); i++) //add items to the list box based on if they're found in the list of current contacts
            {
                String currentFullName = this.contacts.get(i).getFirstName() + this.contacts.get(i).getMiddleName() + this.contacts.get(i).getLastName(); //TODO: Make the currentFullName all lowercase as well as all the tempNames lowercase as well
                currentFullName = currentFullName.toLowerCase();
                tempFirstName = tempFirstName.toLowerCase();
                tempLastName = tempLastName.toLowerCase();
                tempMiddleName = tempMiddleName.toLowerCase();
                if(currentFullName.contains(tempLastName.toLowerCase()) && !tempLastName.equals(""))
                {
                    searchBox.getItems().add(listBoxIndex, this.contacts.get(i));//this.contacts.get(i).getLastName() + ", " + this.contacts.get(i).getFirstName() + " " + this.contacts.get(i).getMiddleName());
                }
                if(currentFullName.contains(tempFirstName.toLowerCase()) && !tempFirstName.equals(""))
                {
                    if(!searchBox.getItems().contains(this.contacts.get(i))) //tests to see if the person has already been added
                    {
                        searchBox.getItems().add(listBoxIndex, this.contacts.get(i));// this.contacts.get(i).getLastName() + ", " + this.contacts.get(i).getFirstName() + " " + this.contacts.get(i).getMiddleName());
                    }
                }
                if(currentFullName.contains(tempMiddleName.toLowerCase()) && !tempMiddleName.equals(""))
                {
                    if(!searchBox.getItems().contains(this.contacts.get(i)))//this.contacts.get(i).getLastName() + ", " + this.contacts.get(i).getFirstName() + " " + this.contacts.get(i).getMiddleName()))
                    {
                        searchBox.getItems().add(listBoxIndex, this.contacts.get(i)); // this.contacts.get(i).getLastName() + ", " + this.contacts.get(i).getFirstName() + " " + this.contacts.get(i).getMiddleName());
                    }
                }
            }
        });

        phoneNumberTextField.setOnAction(action ->
        {
            searchBox.getItems().clear();

            String tempPhoneNumber = phoneNumberTextField.getText();

            for(int i = 0; i < contacts.size(); i++)
            {
                if(this.contacts.get(i).getPhoneNumbers().get(0).getPhoneNumber().contains(tempPhoneNumber) && !tempPhoneNumber.equals(""))
                {
                    if(!searchBox.getItems().contains(this.contacts.get(i)))
                    {
                        searchBox.getItems().add(listBoxIndex, this.contacts.get(i));
                    }
                }
            }
        });

        addressTextField.setOnAction(action ->
        {
            searchBox.getItems().clear();

            String tempAddress = addressTextField.getText();

            for(int i = 0; i < contacts.size(); i++)//goes through each contact
            {
                if(this.contacts.get(i).getAddresses().get(0).getStreetAddress().contains(tempAddress) && !tempAddress.equals(""))
                {
                    if(!searchBox.getItems().contains(this.contacts.get(i)))
                    {
                        searchBox.getItems().add(listBoxIndex, this.contacts.get(i));
                    }
                }
            }
        });

        //Buttons
        selectButton.setOnAction(action ->
        {
            tempStage.close();
        });

        cancelButton.setOnAction(action ->
        {
            tempStage.close();
        });

        tempPane.getChildren().addAll(advancedSearch, nameTextField, phoneNumberTextField, addressTextField ,searchBox, selectButton, cancelButton);
        Scene tempScene = new Scene(tempPane);
        tempStage.setScene(tempScene);
        tempStage.showAndWait();
        //ListBox
        return searchBox.getSelectionModel().getSelectedItem();
    }

    private Person addPersonWindow() //opens a window enabling the user to add someone to their list of contacts & writes it out to the file
    {
        //local variables
        final int xInitial = 830;
        final int xIncrement = 200;
        final int yIncrement = 10;
        Person newPerson = new Person(); //Person object to save to contacts.txt
        final File filePath = new File("C:\\Creeper\\" + this.currentUser.getUsername() + "\\contacts.txt");//File Path...Won't change
        String[] stateAbb = {"--", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO" , "MS" , "MT" , "NC" , "ND" , "NE" , "NH" , "NJ" , "NM" , "NV" , "NY" , "OH" , "OK" , "OR" , "PA" , "RI" , "SC" , "SD", "TN" , "TX" , "UT" , "VA", "VT", "WA", "WI", "WV", "WY"};
        String[] addressTypes = {"--", "Home", "Work", "Other"};
        String[] phoneNumberTypes = {"--", "Home", "Cell", "Work", "Other"};
        String[] emailTypes = {"--", "Personal", "Work", "Other"};
        String[] eyeColorTypes = {"--", "Brown", "Blue", "Hazel", "Amber", "Green", "Grey"};
        String[] hairColorTypes = {"--", "Brown", "Black", "Blond", "Red", "White"};

        //ArrayLists
          //Buttons
          ArrayList<Button> removeAddressButtons = new ArrayList<>();
          ArrayList<Button> removePhoneNumberButtons = new ArrayList<>();
          ArrayList<Button> removeEmailButtons = new ArrayList<>();

          //TextFields
            //Address
            ArrayList<TextField> streetAddressTextFields = new ArrayList<>();
            ArrayList<TextField> cityTextFields = new ArrayList<>();
            ArrayList<TextField> zipTextFields = new ArrayList<>();
          //Phone# & email
          ArrayList<TextField> phoneNumberTextFields = new ArrayList<>();
          ArrayList<TextField> emailTextFields = new ArrayList<>();

          //Combo Box's
          ArrayList<ComboBox> stateComboBoxes = new ArrayList<>();
          ArrayList<ComboBox> addressComboBoxes = new ArrayList<>();
          ArrayList<ComboBox> phoneNumberComboBoxes = new ArrayList<>();
          ArrayList<ComboBox> emailComboBoxes = new ArrayList<>();

        //make a new stage and pane
        Stage tempStage = new Stage();tempStage.initModality(Modality.APPLICATION_MODAL);tempStage.setTitle("Add Contact");tempStage.setWidth(850);tempStage.setHeight(1000);tempStage.setResizable(false); //details for the new window
        Pane tempPane = new Pane();
        ScrollPane sp = new ScrollPane();
        sp.setContent(tempPane);
        sp.setPannable(true);

        try //set the icon image
        {
            tempStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            popUpErrorWindow("ERROR: " + e.toString());
        }

        //add default buttons & textfields to arraylist
        Button initialButton = new Button("+");initialButton.setMinHeight(20);initialButton.setMinWidth(40);initialButton.setShape(new Circle(1.5));initialButton.setMaxSize(25,25);initialButton.setMinSize(25, 25);

        //make all nodes in add person window
        //Tool Tips
        final Tooltip measurementToolTip = new Tooltip();measurementToolTip.setText("You can change your measurement \npreference in Account Settings");measurementToolTip.setShowDelay(new Duration(20));

        //Text Objects
        Text nameTitleText = new Text("Name");nameTitleText.setX(tempStage.getWidth() - xInitial);nameTitleText.setY(tempStage.getHeight() - 970);nameTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text dateOfBirthTitleText = new Text("Date of Birth");dateOfBirthTitleText.setX(tempStage.getWidth() - xInitial);dateOfBirthTitleText.setY(nameTitleText.getY() + (yIncrement * 7));dateOfBirthTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text sexTitleText = new Text("Sex");sexTitleText.setX(dateOfBirthTitleText.getX() + xIncrement);sexTitleText.setY(dateOfBirthTitleText.getY());sexTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text weightTitleText = new Text("Weight");weightTitleText.setX(dateOfBirthTitleText.getX());weightTitleText.setY(dateOfBirthTitleText.getY() + (yIncrement * 7));weightTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text weightHelperText = new Text();weightHelperText.setX(weightTitleText.getX() + 65);weightHelperText.setY(weightTitleText.getY() + (yIncrement * 2.9));weightHelperText.setFont(Font.font("Times New Roman", 16));Tooltip.install(weightHelperText, measurementToolTip);
        Text heightTitleText = new Text("Height");heightTitleText.setX(weightTitleText.getX() + xIncrement);heightTitleText.setY(weightTitleText.getY());heightTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text heightHelperText = new Text();heightHelperText.setX(heightTitleText.getX() + 65);heightHelperText.setY(weightHelperText.getY());heightHelperText.setFont(Font.font("Times New Roman", 16));Tooltip.install(heightHelperText, measurementToolTip);
        Text eyeColorTitleText = new Text("Eye Color");eyeColorTitleText.setX(heightTitleText.getX() + xIncrement);eyeColorTitleText.setY(weightTitleText.getY());eyeColorTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text hairColorTitleText = new Text("Hair Color");hairColorTitleText.setX(eyeColorTitleText.getX() + xIncrement);hairColorTitleText.setY(eyeColorTitleText.getY());hairColorTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text addressTitleText = new Text("Address");addressTitleText.setX(tempStage.getWidth() - xInitial);addressTitleText.setY(dateOfBirthTitleText.getY() + yIncrement * 14);addressTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text phoneNumberTitleText = new Text("Phone Number");phoneNumberTitleText.setX(nameTitleText.getX());phoneNumberTitleText.setY(addressTitleText.getY() + yIncrement * 9);phoneNumberTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text emailTitleText = new Text("Email");emailTitleText.setX(phoneNumberTitleText.getX());emailTitleText.setY(phoneNumberTitleText.getY() + yIncrement * 9);emailTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text categoryTitleText = new Text("Category");categoryTitleText.setX(emailTitleText.getX());categoryTitleText.setY(emailTitleText.getY() + yIncrement * 9);categoryTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
        Text commentTitleText = new Text("Comment");commentTitleText.setX(categoryTitleText.getX());commentTitleText.setY(categoryTitleText.getY() + (yIncrement * 14));commentTitleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));

        //Initial TextFields are created & added to their respective ArrayLists
        TextField firstNameTextField = new TextField();firstNameTextField.setLayoutX(nameTitleText.getX());firstNameTextField.setLayoutY(nameTitleText.getY() + 10);firstNameTextField.setPromptText("First Name");
        TextField middleNameTextField = new TextField();middleNameTextField.setLayoutX(nameTitleText.getX() + xIncrement);middleNameTextField.setLayoutY(firstNameTextField.getLayoutY());middleNameTextField.setPromptText("Middle Name");
        TextField lastNameTextField = new TextField();lastNameTextField.setLayoutX(nameTitleText.getX() + (xIncrement * 2));lastNameTextField.setLayoutY(firstNameTextField.getLayoutY());lastNameTextField.setPromptText("Last Name");
        TextField weightTextField = new TextField();weightTextField.setLayoutX(weightTitleText.getX());weightTextField.setLayoutY(weightTitleText.getY() + yIncrement);weightTextField.setPrefWidth(60);weightTextField.setPromptText("Weight");
        TextField heightTextField = new TextField();heightTextField.setLayoutX(heightTitleText.getX());heightTextField.setLayoutY(heightTitleText.getY() + yIncrement);heightTextField.setPrefWidth(60);heightTextField.setPromptText("Height");
        TextField initialStreetAddressTextField = new TextField();initialStreetAddressTextField.setLayoutX(addressTitleText.getX());initialStreetAddressTextField.setLayoutY(addressTitleText.getY() + yIncrement);initialStreetAddressTextField.setPromptText("Street Address");
        streetAddressTextFields.add(initialStreetAddressTextField);
        TextField initialCityAddressTextField = new TextField();initialCityAddressTextField.setLayoutX(addressTitleText.getX() + xIncrement);initialCityAddressTextField.setLayoutY(initialStreetAddressTextField.getLayoutY());initialCityAddressTextField.setPromptText("City / Town");
        cityTextFields.add(initialCityAddressTextField);
        TextField initialZipcodeTextField = new TextField();initialZipcodeTextField.setLayoutX(initialCityAddressTextField.getLayoutX() + (xIncrement * 1.3));initialZipcodeTextField.setLayoutY(initialCityAddressTextField.getLayoutY());initialZipcodeTextField.setPromptText("Zip Code");
        zipTextFields.add(initialZipcodeTextField);
        TextField initialPhoneNumberTextField = new TextField();initialPhoneNumberTextField.setLayoutX(phoneNumberTitleText.getX());initialPhoneNumberTextField.setLayoutY(phoneNumberTitleText.getY() + yIncrement * 1.5);initialPhoneNumberTextField.setPromptText("Phone Number");
        phoneNumberTextFields.add(initialPhoneNumberTextField);
        TextField initialEmailTextField = new TextField();initialEmailTextField.setLayoutX(emailTitleText.getX());initialEmailTextField.setLayoutY(emailTitleText.getY() + yIncrement);initialEmailTextField.setPromptText("Email");
        emailTextFields.add(initialEmailTextField);
        TextField addCategoryTextField = new TextField();addCategoryTextField.setLayoutX(categoryTitleText.getX() + 245);addCategoryTextField.setLayoutY(categoryTitleText.getY() + yIncrement * 8.5);addCategoryTextField.setPromptText("Enter in new category");addCategoryTextField.setVisible(false);

        //Text Area's
        TextArea commentTextArea = new TextArea();commentTextArea.setLayoutX(commentTitleText.getX());commentTextArea.setLayoutY(commentTitleText.getY() + (yIncrement * 1.5));commentTextArea.setPrefWidth(400);commentTextArea.setPrefHeight(200);commentTextArea.setWrapText(true);commentTextArea.setPromptText("Enter comment here...");

        //Radio Buttons
        ToggleGroup sexes = new ToggleGroup();
        RadioButton maleRadioButton = new RadioButton("Male");maleRadioButton.setLayoutX(sexTitleText.getX());maleRadioButton.setLayoutY(sexTitleText.getY() + yIncrement * (1.5));
        RadioButton femaleRadioButton = new RadioButton("Female");femaleRadioButton.setLayoutX(sexTitleText.getX() + 60);femaleRadioButton.setLayoutY(maleRadioButton.getLayoutY());

        maleRadioButton.setToggleGroup(sexes);
        femaleRadioButton.setToggleGroup(sexes);

        //Datepicker
        TextField dateOfBirth = new TextField();dateOfBirth.setLayoutX(nameTitleText.getX());dateOfBirth.setLayoutY(dateOfBirthTitleText.getY() + yIncrement);dateOfBirth.setPromptText("MM/dd/YYYY");

        //Initial ComboBox's are created & added to their respective ArrayLists
        ComboBox<String> eyecolorComboBox = new ComboBox<>(FXCollections.observableArrayList(eyeColorTypes));eyecolorComboBox.setLayoutX(eyeColorTitleText.getX());eyecolorComboBox.setLayoutY(eyeColorTitleText.getY() + yIncrement);eyecolorComboBox.getSelectionModel().selectFirst();
        ComboBox<String> hairColorComboBox = new ComboBox<>(FXCollections.observableArrayList(hairColorTypes));hairColorComboBox.setLayoutX(hairColorTitleText.getX());hairColorComboBox.setLayoutY(hairColorTitleText.getY() + yIncrement);hairColorComboBox.getSelectionModel().selectFirst();
        ComboBox<String> stateComboBox = new ComboBox<>(FXCollections.observableArrayList(stateAbb));stateComboBox.setLayoutX(initialCityAddressTextField.getLayoutX() + (xIncrement * .85));stateComboBox.setLayoutY(initialCityAddressTextField.getLayoutY());stateComboBox.setMinWidth(65);stateComboBox.setMaxWidth(65);stateComboBox.getSelectionModel().selectFirst();
        stateComboBoxes.add(stateComboBox);
        ComboBox<String> addressTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(addressTypes));addressTypeComboBox.setLayoutX(stateComboBox.getLayoutX() + (xIncrement * 1.25));addressTypeComboBox.setLayoutY(stateComboBox.getLayoutY());addressTypeComboBox.setMinWidth(75);addressTypeComboBox.setMaxWidth(75);addressTypeComboBox.getSelectionModel().selectFirst();
        addressComboBoxes.add(addressTypeComboBox);
        ComboBox<String> phoneNumberTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(phoneNumberTypes));phoneNumberTypeComboBox.setLayoutX(initialPhoneNumberTextField.getLayoutX() + (xIncrement * .8));phoneNumberTypeComboBox.setLayoutY(initialPhoneNumberTextField.getLayoutY());phoneNumberTypeComboBox.setMaxWidth(75);phoneNumberTypeComboBox.setMinWidth(75);phoneNumberTypeComboBox.getSelectionModel().selectFirst();
        phoneNumberComboBoxes.add(phoneNumberTypeComboBox);
        ComboBox<String> emailTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(emailTypes));emailTypeComboBox.setLayoutX(phoneNumberTypeComboBox.getLayoutX());emailTypeComboBox.setLayoutY(initialEmailTextField.getLayoutY());emailTypeComboBox.setMinWidth(75);emailTypeComboBox.setMaxWidth(75);emailTypeComboBox.getSelectionModel().selectFirst();
        emailComboBoxes.add(emailTypeComboBox);

        //add the newly made fields to pane
        tempPane.getChildren().addAll(streetAddressTextFields.get(0), cityTextFields.get(0), zipTextFields.get(0), stateComboBoxes.get(0), addressComboBoxes.get(0), phoneNumberTextFields.get(0), emailTextFields.get(0));

        //ListView
        ListView<String> categoryListView = new ListView<>(FXCollections.observableArrayList(this.currentUser.getCategories()));categoryListView.setLayoutX(categoryTitleText.getX());categoryListView.setLayoutY(categoryTitleText.getY() + yIncrement);categoryListView.setPrefHeight(100);categoryListView.setPrefWidth(235);categoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Buttons
        Button initialAddAddressPlusButton = new Button("+");initialAddAddressPlusButton.setMinHeight(20);initialAddAddressPlusButton.setMinWidth(40);initialAddAddressPlusButton.setShape(new Circle(1.5));initialAddAddressPlusButton.setMaxSize(25,25);initialAddAddressPlusButton.setMinSize(25, 25);initialAddAddressPlusButton.setLayoutX(addressTypeComboBox.getLayoutX() + (xIncrement * .43));initialAddAddressPlusButton.setLayoutY(initialStreetAddressTextField.getLayoutY());
        Button initialMinusAddressButton = new Button("-");initialMinusAddressButton.setMinHeight(20);initialMinusAddressButton.setMinWidth(20);initialMinusAddressButton.setLayoutX(addressTypeComboBox.getLayoutX() + (xIncrement * .43));initialMinusAddressButton.setLayoutY(initialAddAddressPlusButton.getLayoutY());initialMinusAddressButton.setShape(new Circle(1.5));initialMinusAddressButton.setMaxSize(25, 25);initialMinusAddressButton.setMinSize(25, 25);initialMinusAddressButton.setVisible(false);
        Button initialAddPhoneNumberPlusButton = new Button("+");initialAddPhoneNumberPlusButton.setMinHeight(20);initialAddPhoneNumberPlusButton.setMinWidth(40);initialAddPhoneNumberPlusButton.setShape(new Circle(1.5));initialAddPhoneNumberPlusButton.setMaxSize(25,25);initialAddPhoneNumberPlusButton.setMinSize(25, 25);initialAddPhoneNumberPlusButton.setLayoutX(phoneNumberTypeComboBox.getLayoutX() + (xIncrement * .43));initialAddPhoneNumberPlusButton.setLayoutY(phoneNumberTypeComboBox.getLayoutY());
        Button initialMinusPhoneNumberButton = new Button("-");initialMinusPhoneNumberButton.setMinHeight(20);initialMinusPhoneNumberButton.setMinWidth(40);initialMinusPhoneNumberButton.setShape(new Circle(1.5));initialMinusPhoneNumberButton.setMaxSize(25,25);initialMinusPhoneNumberButton.setMinSize(25, 25);initialMinusPhoneNumberButton.setLayoutX(initialAddPhoneNumberPlusButton.getLayoutX());initialMinusPhoneNumberButton.setLayoutY(initialAddPhoneNumberPlusButton.getLayoutY());
        Button initialAddEmailPlusButton = new Button("+");initialAddEmailPlusButton.setMinHeight(20);initialAddEmailPlusButton.setMinWidth(40);initialAddEmailPlusButton.setShape(new Circle(1.5));initialAddEmailPlusButton.setMaxSize(25,25);initialAddEmailPlusButton.setMinSize(25, 25);initialAddEmailPlusButton.setLayoutX(initialAddPhoneNumberPlusButton.getLayoutX());initialAddEmailPlusButton.setLayoutY(initialEmailTextField.getLayoutY());
        Button initialMinusEmailButton = new Button("-");initialMinusEmailButton.setMinHeight(20);initialMinusEmailButton.setMinWidth(40);initialMinusEmailButton.setShape(new Circle(1.5));initialMinusEmailButton.setMaxSize(25,25);initialMinusEmailButton.setMinSize(25, 25);initialMinusEmailButton.setLayoutX(initialAddEmailPlusButton.getLayoutX());initialMinusEmailButton.setLayoutY(initialAddEmailPlusButton.getLayoutY());
        Button cancelButton = new Button("Cancel");cancelButton.setMinHeight(50);cancelButton.setMinWidth(100);cancelButton.setLayoutX(tempStage.getWidth() - 130); cancelButton.setLayoutY(tempStage.getHeight() - 100);
        Button addPersonButton = new Button("Submit");addPersonButton.setMinHeight(50);addPersonButton.setMinWidth(100);addPersonButton.setLayoutX(tempStage.getWidth() - xInitial);addPersonButton.setLayoutY(cancelButton.getLayoutY());
        Button addCategoryPlusButton = new Button(">");addCategoryPlusButton.setMinHeight(20);addCategoryPlusButton.setMinWidth(40);addCategoryPlusButton.setLayoutX(initialMinusPhoneNumberButton.getLayoutX());addCategoryPlusButton.setLayoutY(categoryListView.getLayoutY());addCategoryPlusButton.setShape(new Circle(1.5));addCategoryPlusButton.setMaxSize(25,25);addCategoryPlusButton.setMinSize(25, 25);
        Button removeCategoryMinusButton = new Button("<");removeCategoryMinusButton.setMinHeight(20);removeCategoryMinusButton.setMinWidth(40);removeCategoryMinusButton.setLayoutX(addCategoryPlusButton.getLayoutX());removeCategoryMinusButton.setLayoutY(addCategoryPlusButton.getLayoutY());removeCategoryMinusButton.setShape(new Circle(1.5));removeCategoryMinusButton.setMaxSize(25, 25);removeCategoryMinusButton.setMinSize(25, 25);removeCategoryMinusButton.setVisible(false);
        Button addCategoryButton = new Button("Add");addCategoryButton.setMinHeight(20);addCategoryButton.setMinWidth(40);addCategoryButton.setLayoutX(addCategoryTextField.getLayoutX() + (xIncrement * .78));addCategoryButton.setLayoutY(addCategoryTextField.getLayoutY());addCategoryButton.setVisible(false);
        Button removeCategoryButton = new Button("Remove");removeCategoryButton.setPrefHeight(20);removeCategoryButton.setPrefWidth(70);removeCategoryButton.setLayoutX(addCategoryPlusButton.getLayoutX());removeCategoryButton.setLayoutY(addCategoryTextField.getLayoutY());

        //NODE FUNCTIONALITY
        //Text
        if(currentUser.getIsMetricUser())
        {
            weightHelperText.setText("Kg");
            heightHelperText.setText("cm");
        }
        else
        {
            weightHelperText.setText("lbs");
            heightHelperText.setText("in");
        }

        //Buttons
        initialAddAddressPlusButton.setOnAction(action ->
        {
            Button newButton = new Button("-");newButton.setMinHeight(20);newButton.setMinWidth(20);newButton.setLayoutX(addressTypeComboBox.getLayoutX() + (xIncrement * .43));newButton.setLayoutY(initialAddAddressPlusButton.getLayoutY());newButton.setShape(new Circle(1.5));newButton.setMaxSize(25, 25);newButton.setMinSize(25, 25);
            newButton.setOnAction(function -> //create the action for the button before you add it to the array
            {
                /* GUIDE TO WHAT THIS STUPIDLY COMPLICATED BUTTON DOES
                 * -----------------------------------------------------
                 * 1) Make sure that if there's only one removeAddressButton left, to hide it from the pane and adjust the position of the addressPlusButton
                 * 2) Shifts all the nodes on the screen up accordingly
                 * 3) Redraw the relevant nodes (addressTextField, cityTextField, stateComboBox, zipTextField, addressComboBox, removeAddressButton)
                 *  3a) Delete the nodes from the pane
                 *  3b) Delete the appropriate nodes from the their corresponding ArrayLists
                 *  3c) add the modified list of nodes to the pane
                 * 4) Make sure that if there's only one removeAddressButton left, to hide it from the pane
                 */

                //1) Shift all nodes on the pane up accordingly
                //1a)Shift all the non-array nodes up
                initialAddAddressPlusButton.setLayoutY(initialAddAddressPlusButton.getLayoutY() - (yIncrement * 3));
                initialMinusAddressButton.setLayoutY(initialMinusAddressButton.getLayoutY() - (yIncrement * 3));
                phoneNumberTitleText.setY(phoneNumberTitleText.getY() - (yIncrement * 3));
                initialAddPhoneNumberPlusButton.setLayoutY(initialAddPhoneNumberPlusButton.getLayoutY() - (yIncrement * 3));
                initialMinusPhoneNumberButton.setLayoutY(initialMinusPhoneNumberButton.getLayoutY() - (yIncrement * 3));
                emailTitleText.setY(emailTitleText.getY() - (yIncrement * 3));
                initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() - (yIncrement * 3));
                initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() - (yIncrement * 3));
                categoryTitleText.setY(categoryTitleText.getY() - (yIncrement * 3));
                categoryListView.setLayoutY(categoryListView.getLayoutY() - (yIncrement * 3));
                addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() - (yIncrement * 3));
                removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() - (yIncrement * 3));
                addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() - (yIncrement * 3));
                addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() - (yIncrement * 3));
                removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() - (yIncrement * 3));
                commentTitleText.setY(commentTitleText.getY() - (yIncrement * 3));
                commentTextArea.setLayoutY(commentTextArea.getLayoutY() - (yIncrement * 3));
                cancelButton.setLayoutY(cancelButton.getLayoutY() - (yIncrement * 3));
                addPersonButton.setLayoutY(addPersonButton.getLayoutY() - (yIncrement * 3));

                //1b)Shift all the phone number & email ArrayLists up
                for(int i = 0; i < removePhoneNumberButtons.size(); i++)
                {
                    removePhoneNumberButtons.get(i).setLayoutY(removePhoneNumberButtons.get(i).getLayoutY() - (yIncrement * 3));
                }
                for(int i = 0; i < phoneNumberTextFields.size(); i++)
                {
                    phoneNumberTextFields.get(i).setLayoutY(phoneNumberTextFields.get(i).getLayoutY() - (yIncrement * 3));
                    phoneNumberComboBoxes.get(i).setLayoutY(phoneNumberComboBoxes.get(i).getLayoutY() - (yIncrement * 3));
                }
                for(int i = 0; i < removeEmailButtons.size(); i ++)
                {
                    removeEmailButtons.get(i).setLayoutY(removeEmailButtons.get(i).getLayoutY() - (yIncrement * 3));
                }
                for(int i = 0; i < emailTextFields.size(); i ++)
                {
                    emailTextFields.get(i).setLayoutY(emailTextFields.get(i).getLayoutY() - (yIncrement * 3));
                    emailComboBoxes.get(i).setLayoutY(emailComboBoxes.get(i).getLayoutY() - (yIncrement * 3));
                }
                //2) Redraw buttons
                //2a) Delete nodes from pane
                for(int i = 0; i < removeAddressButtons.size(); i++)//delete
                {
                    tempPane.getChildren().remove(removeAddressButtons.get(i)); //the actual deleting of buttons from pane
                }
                int unluckyNode = 0;
                for(int i = 0; i < streetAddressTextFields.size(); i++) //delete the nodes associated with that button
                {
                    tempPane.getChildren().removeAll(streetAddressTextFields.get(i), cityTextFields.get(i), stateComboBoxes.get(i), zipTextFields.get(i), addressComboBoxes.get(i));
                    if(streetAddressTextFields.get(i).getLayoutY() == newButton.getLayoutY())//Delete the corresponding Address Box's based on their Y-coordinate as well as remove them from the their respective array lists
                    {
                        unluckyNode = i; //save the unlucky node positions for later
                    }
                }

                //2b) Delete nodes from their respective ArrayLists
                streetAddressTextFields.remove(streetAddressTextFields.get(unluckyNode));
                cityTextFields.remove(cityTextFields.get(unluckyNode));
                stateComboBoxes.remove(stateComboBoxes.get(unluckyNode));
                zipTextFields.remove(zipTextFields.get(unluckyNode));
                addressComboBoxes.remove(addressComboBoxes.get(unluckyNode));

                removeAddressButtons.remove(newButton);//remove button from ArrayList

                //2c) Add nodes back to the pane
                for(int i = 0; i < removeAddressButtons.size(); i++)//redraw buttons
                {
                    removeAddressButtons.get(i).setLayoutY((addressTitleText.getY() + yIncrement) + (yIncrement * 3 * i)); //reset the Y value
                    tempPane.getChildren().add(removeAddressButtons.get(i));
                }

                for(int i = 0; i < streetAddressTextFields.size(); i++)//redraw other nodes
                {
                    //reset Y values
                    streetAddressTextFields.get(i).setLayoutY((addressTitleText.getY() + yIncrement) + (yIncrement * 3 * i));
                    cityTextFields.get(i).setLayoutY(streetAddressTextFields.get(i).getLayoutY());
                    stateComboBoxes.get(i).setLayoutY(streetAddressTextFields.get(i).getLayoutY());
                    zipTextFields.get(i).setLayoutY(streetAddressTextFields.get(i).getLayoutY());
                    addressComboBoxes.get(i).setLayoutY(streetAddressTextFields.get(i).getLayoutY());

                    //add them back to the pane
                    tempPane.getChildren().addAll(streetAddressTextFields.get(i), cityTextFields.get(i), stateComboBoxes.get(i), zipTextFields.get(i), addressComboBoxes.get(i));
                }

                if(removeAddressButtons.size() == 0)
                {
                    initialAddAddressPlusButton.setLayoutX(addressTypeComboBox.getLayoutX() + (xIncrement * .43));
                    initialMinusAddressButton.setVisible(false);
                }
            });
            removeAddressButtons.add(newButton); //add the newly created button to the ArrayList

            for(int i = 0; i < removeAddressButtons.size(); i++) //loop to remove all "-" buttons from the pane
            {
                tempPane.getChildren().remove(removeAddressButtons.get(i));
            }

            for(int i = 0; i < removeAddressButtons.size(); i++) //redraw with the correct amount of "-" buttons(+1)
            {
                tempPane.getChildren().addAll(removeAddressButtons.get(i));
            }
            
            //shift everything below address down accordingly
            initialAddAddressPlusButton.setLayoutY(initialAddAddressPlusButton.getLayoutY() + (yIncrement * 3));
            initialMinusAddressButton.setLayoutY(initialMinusAddressButton.getLayoutY() + (yIncrement * 3));
            phoneNumberTitleText.setY(phoneNumberTitleText.getY() + (yIncrement * 3));
            initialAddPhoneNumberPlusButton.setLayoutY(initialAddPhoneNumberPlusButton.getLayoutY() + (yIncrement * 3));
            initialMinusPhoneNumberButton.setLayoutY(initialMinusPhoneNumberButton.getLayoutY() + (yIncrement * 3));
            emailTitleText.setY(emailTitleText.getY() + (yIncrement * 3));
            initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() + (yIncrement * 3));
            initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() + (yIncrement * 3));
            categoryTitleText.setY(categoryTitleText.getY() + (yIncrement * 3));
            categoryListView.setLayoutY(categoryListView.getLayoutY() + (yIncrement * 3));
            addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() + (yIncrement * 3));
            removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() + (yIncrement * 3));
            addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() + (yIncrement * 3));
            addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() + (yIncrement * 3));
            removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() + (yIncrement * 3));
            commentTitleText.setY(commentTitleText.getY() + (yIncrement * 3));
            commentTextArea.setLayoutY(commentTextArea.getLayoutY() + (yIncrement * 3));
            cancelButton.setLayoutY(cancelButton.getLayoutY() + (yIncrement * 3));
            addPersonButton.setLayoutY(addPersonButton.getLayoutY() + (yIncrement * 3));

            for(int i = 0; i < removePhoneNumberButtons.size(); i++) //shift the "-" phone number buttons down
            {
                removePhoneNumberButtons.get(i).setLayoutY(removePhoneNumberButtons.get(i).getLayoutY() + (yIncrement * 3));
            }
            for(int i = 0; i < phoneNumberTextFields.size(); i++)
            {
                phoneNumberTextFields.get(i).setLayoutY(phoneNumberTextFields.get(i).getLayoutY() + (yIncrement * 3));
                phoneNumberComboBoxes.get(i).setLayoutY(phoneNumberComboBoxes.get(i).getLayoutY() + (yIncrement * 3));
            }
            for(int i = 0; i < removeEmailButtons.size(); i++)//shift the "-" email buttons down
            {
                removeEmailButtons.get(i).setLayoutY(removeEmailButtons.get(i).getLayoutY() + (yIncrement * 3));
            }
            for(int i = 0; i < emailTextFields.size(); i++)
            {
                emailTextFields.get(i).setLayoutY(emailTextFields.get(i).getLayoutY() + (yIncrement * 3));
                emailComboBoxes.get(i).setLayoutY(emailComboBoxes.get(i).getLayoutY() + (yIncrement * 3));
            }

            //add another row of address elements to the page
            //generate their properties
            TextField newStAddress = new TextField();newStAddress.setLayoutX(addressTitleText.getX());newStAddress.setLayoutY(initialStreetAddressTextField.getLayoutY() + ((yIncrement * 3) * streetAddressTextFields.size()));newStAddress.setPromptText("Street Address");
            streetAddressTextFields.add(newStAddress);
            TextField newCity = new TextField();newCity.setLayoutX(initialCityAddressTextField.getLayoutX());newCity.setLayoutY(initialCityAddressTextField.getLayoutY() + ((yIncrement * 3) * cityTextFields.size()));newCity.setPromptText("City / Town");
            cityTextFields.add(newCity);
            ComboBox<String> newState = new ComboBox<>(FXCollections.observableArrayList(stateAbb));newState.setLayoutX(stateComboBox.getLayoutX());newState.setLayoutY(stateComboBox.getLayoutY() + ((yIncrement * 3) * stateComboBoxes.size()));newState.setMinWidth(65);newState.setMaxWidth(65);newState.getSelectionModel().selectFirst();
            stateComboBoxes.add(newState);
            TextField newZip = new TextField();newZip.setLayoutX(initialZipcodeTextField.getLayoutX());newZip.setLayoutY(initialZipcodeTextField.getLayoutY() + ((yIncrement * 3) * zipTextFields.size()));newZip.setPromptText("Zip Code");
            zipTextFields.add(newZip);
            ComboBox<String> newAddressType = new ComboBox<>(FXCollections.observableArrayList(addressTypes));newAddressType.setLayoutX(addressTypeComboBox.getLayoutX());newAddressType.setLayoutY(addressTypeComboBox.getLayoutY() + ((yIncrement * 3) * addressComboBoxes.size()));newAddressType.getSelectionModel().selectFirst();
            addressComboBoxes.add(newAddressType);

            //add them to the pane
            for(int i = 0; i < streetAddressTextFields.size(); i++) //delete all the text fields before I redraw them
            {
                tempPane.getChildren().removeAll(streetAddressTextFields.get(i), cityTextFields.get(i), stateComboBoxes.get(i), zipTextFields.get(i), addressComboBoxes.get(i));
            }

            for(int i = 0; i < streetAddressTextFields.size(); i++)
            {
                tempPane.getChildren().addAll(streetAddressTextFields.get(i), cityTextFields.get(i), stateComboBoxes.get(i), zipTextFields.get(i), addressComboBoxes.get(i));
            }

            //if there's more than one removeAddressButtons, then displace the add button to the right
            if(removeAddressButtons.size() > 0)
            {
                initialAddAddressPlusButton.setLayoutX((addressTypeComboBox.getLayoutX() + (xIncrement * .43)) + 30);
                initialMinusAddressButton.setVisible(true);
            }
        });
        initialMinusAddressButton.setOnAction(action ->
        {
            //move everything up
            initialAddAddressPlusButton.setLayoutY(initialAddAddressPlusButton.getLayoutY() - (yIncrement * 3));
            initialMinusAddressButton.setLayoutY(initialMinusAddressButton.getLayoutY() - (yIncrement * 3));
            phoneNumberTitleText.setY(phoneNumberTitleText.getY() - (yIncrement * 3));
            initialAddPhoneNumberPlusButton.setLayoutY(initialAddPhoneNumberPlusButton.getLayoutY() - (yIncrement * 3));
            initialMinusPhoneNumberButton.setLayoutY(initialMinusPhoneNumberButton.getLayoutY() - (yIncrement * 3));
            emailTitleText.setY(emailTitleText.getY() - (yIncrement * 3));
            initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() - (yIncrement * 3));
            initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() - (yIncrement * 3));
            categoryTitleText.setY(categoryTitleText.getY() - (yIncrement * 3));
            categoryListView.setLayoutY(categoryListView.getLayoutY() - (yIncrement * 3));
            addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() - (yIncrement * 3));
            removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() - (yIncrement * 3));
            addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() - (yIncrement * 3));
            addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() - (yIncrement * 3));
            removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() - (yIncrement * 3));
            commentTitleText.setY(commentTitleText.getY() - (yIncrement * 3));
            commentTextArea.setLayoutY(commentTextArea.getLayoutY() - (yIncrement * 3));
            cancelButton.setLayoutY(cancelButton.getLayoutY() - (yIncrement * 3));
            addPersonButton.setLayoutY(addPersonButton.getLayoutY() - (yIncrement * 3));

            //shift all the Phone Number elements down
            for(int i = 0; i < removePhoneNumberButtons.size(); i++)
            {
                removePhoneNumberButtons.get(i).setLayoutY(removePhoneNumberButtons.get(i).getLayoutY() - (yIncrement * 3));
            }
            for(int i = 0; i < phoneNumberTextFields.size(); i++)
            {
                phoneNumberTextFields.get(i).setLayoutY(phoneNumberTextFields.get(i).getLayoutY() - (yIncrement * 3));
                phoneNumberComboBoxes.get(i).setLayoutY(phoneNumberComboBoxes.get(i).getLayoutY() - (yIncrement * 3));
            }
            //Shift all the Email elements down
            for(int i = 0; i < removeEmailButtons.size(); i++)
            {
                removeEmailButtons.get(i).setLayoutY(removeEmailButtons.get(i).getLayoutY() - (yIncrement * 3));
            }

            for(int i = 0; i < emailTextFields.size(); i++)
            {
                emailTextFields.get(i).setLayoutY(emailTextFields.get(i).getLayoutY() - (yIncrement * 3));
                emailComboBoxes.get(i).setLayoutY(emailComboBoxes.get(i).getLayoutY() - (yIncrement * 3));
            }


            //redraw
            //delete the nodes from the pane
            for(int i = 0; i < removeAddressButtons.size(); i++)
            {
                tempPane.getChildren().remove(removeAddressButtons.get(i));
            }
            for(int i = 0; i < streetAddressTextFields.size(); i++)
            {
                tempPane.getChildren().removeAll(streetAddressTextFields.get(i), cityTextFields.get(i), stateComboBoxes.get(i), zipTextFields.get(i), addressComboBoxes.get(i));
            }
            //delete the nodes from their respective arraylists
            removeAddressButtons.remove(removeAddressButtons.get(removeAddressButtons.size() - 1)); //removes the last element
            streetAddressTextFields.remove(streetAddressTextFields.get(streetAddressTextFields.size() - 1));
            cityTextFields.remove(cityTextFields.get(cityTextFields.size() - 1));
            stateComboBoxes.remove(stateComboBoxes.get(stateComboBoxes.size() - 1));
            zipTextFields.remove(zipTextFields.get(zipTextFields.size() - 1));
            addressComboBoxes.remove(addressComboBoxes.get(addressComboBoxes.size() - 1));

            //add nodes back into the pane
            for(int i = 0; i< removeAddressButtons.size(); i++) //add the nodes back to the pane
            {
                //reset the Y coordinate
                removeAddressButtons.get(i).setLayoutY((addressTitleText.getY() + yIncrement) + (yIncrement * 3 * i));

                //add the nodes back to the pane
                tempPane.getChildren().add(removeAddressButtons.get(i));
            }

            for(int i = 0; i < streetAddressTextFields.size(); i++)//redraw other nodes
            {
                //reset Y values
                streetAddressTextFields.get(i).setLayoutY((addressTitleText.getY() + yIncrement) + (yIncrement * 3 * i));
                cityTextFields.get(i).setLayoutY(streetAddressTextFields.get(i).getLayoutY());
                stateComboBoxes.get(i).setLayoutY(streetAddressTextFields.get(i).getLayoutY());
                zipTextFields.get(i).setLayoutY(streetAddressTextFields.get(i).getLayoutY());
                addressComboBoxes.get(i).setLayoutY(streetAddressTextFields.get(i).getLayoutY());

                //add them back to the pane
                tempPane.getChildren().addAll(streetAddressTextFields.get(i), cityTextFields.get(i), stateComboBoxes.get(i), zipTextFields.get(i), addressComboBoxes.get(i));
            }

            if(removeAddressButtons.size() == 0)
            {
                initialMinusAddressButton.setVisible(false);
                initialAddAddressPlusButton.setLayoutX(addressTypeComboBox.getLayoutX() + (xIncrement * .43));
            }
        });

        initialAddPhoneNumberPlusButton.setOnAction(action ->
        {
            //make a minus button where the plus button was
            Button newButton = new Button("-");newButton.setMinHeight(20);newButton.setMinWidth(20);newButton.setLayoutX(phoneNumberTypeComboBox.getLayoutX() + (xIncrement * .43));newButton.setLayoutY(initialAddPhoneNumberPlusButton.getLayoutY());newButton.setShape(new Circle(1.5));newButton.setMaxSize(25, 25);newButton.setMinSize(25, 25);
            newButton.setOnAction(function ->
            {
                //1)Shift everything up
                initialAddPhoneNumberPlusButton.setLayoutY(initialAddPhoneNumberPlusButton.getLayoutY() - (yIncrement * 3));
                initialMinusPhoneNumberButton.setLayoutY(initialMinusPhoneNumberButton.getLayoutY() - (yIncrement * 3));
                emailTitleText.setY(emailTitleText.getY() - (yIncrement * 3));
                initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() - (yIncrement * 3));
                initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() - (yIncrement * 3));
                categoryTitleText.setY(categoryTitleText.getY() - (yIncrement * 3));
                categoryListView.setLayoutY(categoryListView.getLayoutY() - (yIncrement * 3));
                addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() - (yIncrement * 3));
                removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() - (yIncrement * 3));
                addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() - (yIncrement * 3));
                addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() - (yIncrement * 3));
                removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() - (yIncrement * 3));
                commentTitleText.setY(commentTitleText.getY() - (yIncrement * 3));
                commentTextArea.setLayoutY(commentTextArea.getLayoutY() - (yIncrement * 3));
                cancelButton.setLayoutY(cancelButton.getLayoutY() - (yIncrement * 3));
                addPersonButton.setLayoutY(addPersonButton.getLayoutY() - (yIncrement * 3));

                for(int i = 0; i < removeEmailButtons.size(); i++)
                {
                    removeEmailButtons.get(i).setLayoutY(removeEmailButtons.get(i).getLayoutY() - (yIncrement * 3));
                }

                for(int i = 0; i < emailTextFields.size(); i++)
                {
                    emailTextFields.get(i).setLayoutY(emailTextFields.get(i).getLayoutY() - (yIncrement * 3));
                    emailComboBoxes.get(i).setLayoutY(emailComboBoxes.get(i).getLayoutY() - (yIncrement * 3));
                }

                //2) Redraw buttons
                //2a) Delete nodes from pane
                for(int i = 0; i < removePhoneNumberButtons.size(); i++)//delete
                {
                    tempPane.getChildren().remove(removePhoneNumberButtons.get(i)); //the deleting of buttons from pane
                }
                int unluckyNode = 0;
                for(int i = 0; i < phoneNumberTextFields.size(); i++) //delete the nodes associated with that button
                {
                    double currentYCoordinate = phoneNumberTextFields.get(i).getLayoutY();
                    tempPane.getChildren().removeAll(phoneNumberTextFields.get(i), phoneNumberComboBoxes.get(i));
                    if(currentYCoordinate == newButton.getLayoutY())//Delete the corresponding Address Box's based on their Y-coordinate as well as remove them from the their respective array lists
                    {
                        unluckyNode = i; //save the unlucky node positions for later
                    }
                }

                //2b) Delete nodes from their respective ArrayLists
                phoneNumberTextFields.remove(unluckyNode);
                phoneNumberComboBoxes.remove(unluckyNode);
                removePhoneNumberButtons.remove(newButton);

                //2c) Add nodes back to the pane
                for(int i = 0; i < removePhoneNumberButtons.size(); i++)//redraw buttons
                {
                    removePhoneNumberButtons.get(i).setLayoutY((phoneNumberTitleText.getY() + yIncrement * 1.5) + (yIncrement * 3 * i)); //reset the Y value
                    tempPane.getChildren().add(removePhoneNumberButtons.get(i));
                }

                for(int i = 0; i < phoneNumberTextFields.size(); i++)//redraw other nodes
                {
                    //reset Y values
                    phoneNumberTextFields.get(i).setLayoutY((phoneNumberTitleText.getY() + yIncrement * 1.5) + (yIncrement * 3 * i));
                    phoneNumberComboBoxes.get(i).setLayoutY(phoneNumberTextFields.get(i).getLayoutY());

                    //add them back to the pane
                    tempPane.getChildren().addAll(phoneNumberTextFields.get(i), phoneNumberComboBoxes.get(i));
                }

                if(removePhoneNumberButtons.size() == 0)
                {
                    initialAddPhoneNumberPlusButton.setLayoutX(phoneNumberTypeComboBox.getLayoutX() + (xIncrement * .43));
                    initialMinusPhoneNumberButton.setVisible(false);
                }
            });
            removePhoneNumberButtons.add(newButton);

            //remove & add the removePhoneNumberButtons
            for(int i = 0; i < removePhoneNumberButtons.size(); i++)
            {
                tempPane.getChildren().remove(removePhoneNumberButtons.get(i));
            }

            for(int i = 0; i < removePhoneNumberButtons.size(); i++)
            {
                tempPane.getChildren().add(removePhoneNumberButtons.get(i));
            }

            //shift everything below this down accordingly
            initialAddPhoneNumberPlusButton.setLayoutY(initialAddPhoneNumberPlusButton.getLayoutY() + (yIncrement * 3));
            initialMinusPhoneNumberButton.setLayoutY(initialMinusPhoneNumberButton.getLayoutY() + (yIncrement * 3));
            emailTitleText.setY(emailTitleText.getY() + (yIncrement * 3));
            initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() + (yIncrement * 3));
            initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() + (yIncrement * 3));
            categoryTitleText.setY(categoryTitleText.getY() + (yIncrement * 3));
            categoryListView.setLayoutY(categoryListView.getLayoutY() + (yIncrement * 3));
            addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() + (yIncrement * 3));
            removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() + (yIncrement * 3));
            addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() + (yIncrement * 3));
            addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() + (yIncrement * 3));
            removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() + (yIncrement * 3));
            commentTitleText.setY(commentTitleText.getY() + (yIncrement * 3));
            commentTextArea.setLayoutY(commentTextArea.getLayoutY() + (yIncrement * 3));
            cancelButton.setLayoutY(cancelButton.getLayoutY() + (yIncrement * 3));
            addPersonButton.setLayoutY(addPersonButton.getLayoutY() + (yIncrement * 3));
            for(int i = 0; i < removeEmailButtons.size(); i++) //shift all the "-" buttons down
            {
                removeEmailButtons.get(i).setLayoutY(removeEmailButtons.get(i).getLayoutY() + (yIncrement * 3));
            }
            for(int i = 0; i < emailTextFields.size(); i++)
            {
                emailTextFields.get(i).setLayoutY(emailTextFields.get(i).getLayoutY() + (yIncrement * 3));
                emailComboBoxes.get(i).setLayoutY(emailComboBoxes.get(i).getLayoutY() + (yIncrement * 3));
            }

            //add another round of elements to the page
            //generate their properties
            TextField newPhoneNumberTxtBox = new TextField();newPhoneNumberTxtBox.setLayoutX(initialPhoneNumberTextField.getLayoutX());newPhoneNumberTxtBox.setLayoutY(initialPhoneNumberTextField.getLayoutY() + (yIncrement * 3) * phoneNumberTextFields.size());newPhoneNumberTxtBox.setPromptText("Phone Number");
            phoneNumberTextFields.add(newPhoneNumberTxtBox);
            ComboBox<String> newPhoneNumberTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(phoneNumberTypes));newPhoneNumberTypeComboBox.setLayoutX(phoneNumberTypeComboBox.getLayoutX());newPhoneNumberTypeComboBox.setLayoutY(phoneNumberTypeComboBox.getLayoutY() + (yIncrement * 3) * phoneNumberComboBoxes.size());newPhoneNumberTypeComboBox.getSelectionModel().selectFirst();
            phoneNumberComboBoxes.add(newPhoneNumberTypeComboBox);

            //redraw
            for(int i = 0; i < phoneNumberTextFields.size(); i++)//delete
            {
                tempPane.getChildren().removeAll(phoneNumberTextFields.get(i), phoneNumberComboBoxes.get(i));
            }
            for(int i = 0; i < phoneNumberTextFields.size(); i++)//add
            {
                tempPane.getChildren().addAll(phoneNumberTextFields.get(i), phoneNumberComboBoxes.get(i));
            }

            if(phoneNumberTextFields.size() == 0)
            {
                phoneNumberTextFields.get(0).setLayoutY((phoneNumberTitleText.getY() + yIncrement * 2));
                phoneNumberComboBoxes.get(0).setLayoutY((phoneNumberTitleText.getY() + yIncrement * 2));
            }

            if(removePhoneNumberButtons.size() > 0)
            {
                initialAddPhoneNumberPlusButton.setLayoutX(phoneNumberTypeComboBox.getLayoutX() + (xIncrement * .43) + 30);
                initialMinusPhoneNumberButton.setVisible(true);
            }
        });
        initialMinusPhoneNumberButton.setOnAction(action ->
        {
            initialAddPhoneNumberPlusButton.setLayoutY(initialAddPhoneNumberPlusButton.getLayoutY() - (yIncrement * 3));
            emailTitleText.setY(emailTitleText.getY() - (yIncrement * 3));
            initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() - (yIncrement * 3));
            initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() - (yIncrement * 3));
            categoryTitleText.setY(categoryTitleText.getY() - (yIncrement * 3));
            categoryListView.setLayoutY(categoryListView.getLayoutY() - (yIncrement * 3));
            addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() - (yIncrement * 3));
            removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() - (yIncrement * 3));
            addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() - (yIncrement * 3));
            addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() - (yIncrement * 3));
            initialMinusPhoneNumberButton.setLayoutY(initialMinusPhoneNumberButton.getLayoutY() - (yIncrement * 3));
            removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() - (yIncrement * 3));
            commentTitleText.setY(commentTitleText.getY() - (yIncrement * 3));
            commentTextArea.setLayoutY(commentTextArea.getLayoutY() - (yIncrement * 3));
            cancelButton.setLayoutY(cancelButton.getLayoutY() - (yIncrement * 3));
            addPersonButton.setLayoutY(addPersonButton.getLayoutY() - (yIncrement * 3));

            //Shift all of the email elements down
            for(int i = 0; i < removeEmailButtons.size(); i++)
            {
                removeEmailButtons.get(i).setLayoutY(removeEmailButtons.get(i).getLayoutY() - (yIncrement * 3));
            }

            for(int i = 0; i < emailTextFields.size(); i++)
            {
                emailTextFields.get(i).setLayoutY(emailTextFields.get(i).getLayoutY() - (yIncrement * 3));
                emailComboBoxes.get(i).setLayoutY(emailComboBoxes.get(i).getLayoutY() - (yIncrement * 3));
            }

            //redraw
            //delete the minusPhoneNumberButtons nodes from the pane
            for(int i = 0; i < removePhoneNumberButtons.size(); i++)
            {
                tempPane.getChildren().remove(removePhoneNumberButtons.get(i));
            }
            for(int i = 0; i < phoneNumberTextFields.size(); i ++)
            {
                tempPane.getChildren().removeAll(phoneNumberTextFields.get(i), phoneNumberComboBoxes.get(i));
            }

            //remove nodes from their respective ArrayLists
            removePhoneNumberButtons.remove(removePhoneNumberButtons.size() - 1);
            phoneNumberTextFields.remove(phoneNumberTextFields.size() - 1);
            phoneNumberComboBoxes.remove(phoneNumberComboBoxes.size() - 1);

            //reAdd them to the pane
            for(int i = 0; i < removePhoneNumberButtons.size(); i++)
            {
                removePhoneNumberButtons.get(i).setLayoutY((phoneNumberTitleText.getY() + yIncrement * 1.5) + (yIncrement * 3 * i)); //reset the Y value
                tempPane.getChildren().add(removePhoneNumberButtons.get(i));
            }

            for(int i = 0; i < phoneNumberTextFields.size(); i++)
            {
                //reset Y values
                phoneNumberTextFields.get(i).setLayoutY((phoneNumberTitleText.getY() + yIncrement * 1.5) + (yIncrement * 3 * i));
                phoneNumberComboBoxes.get(i).setLayoutY(phoneNumberTextFields.get(i).getLayoutY());

                //add them back to the pane
                tempPane.getChildren().addAll(phoneNumberTextFields.get(i), phoneNumberComboBoxes.get(i));
            }

            if(removePhoneNumberButtons.size() == 0)
            {
                initialMinusPhoneNumberButton.setVisible(false);
                initialAddPhoneNumberPlusButton.setLayoutX(phoneNumberTypeComboBox.getLayoutX() + (xIncrement * .43));
            }
        });

        initialAddEmailPlusButton.setOnAction(action ->
        {
            //make a minus button where the plus button was
            Button newButton = new Button("-");newButton.setMinHeight(20);newButton.setMinWidth(20);newButton.setLayoutX(emailTypeComboBox.getLayoutX() + (xIncrement * .43));newButton.setLayoutY(initialAddEmailPlusButton.getLayoutY());newButton.setShape(new Circle(1.5));newButton.setMaxSize(25, 25);newButton.setMinSize(25, 25);
            newButton.setOnAction(function ->
            {
                //1)Shift everything up
                initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() - (yIncrement * 3));
                initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() - (yIncrement * 3));
                categoryTitleText.setY(categoryTitleText.getY() - (yIncrement * 3));
                categoryListView.setLayoutY(categoryListView.getLayoutY() - (yIncrement * 3));
                addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() - (yIncrement * 3));
                removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() - (yIncrement * 3));
                addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() - (yIncrement * 3));
                addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() - (yIncrement * 3));
                removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() - (yIncrement * 3));
                commentTitleText.setY(commentTitleText.getY() - (yIncrement * 3));
                commentTextArea.setLayoutY(commentTextArea.getLayoutY() - (yIncrement * 3));
                cancelButton.setLayoutY(cancelButton.getLayoutY() - (yIncrement * 3));
                addPersonButton.setLayoutY(addPersonButton.getLayoutY() - (yIncrement * 3));

                //Redraw Buttons
                for (int i = 0; i < removeEmailButtons.size(); i++)
                {
                    tempPane.getChildren().remove(removeEmailButtons.get(i));
                }
                int unluckyNode = 0;
                for (int i = 0; i < emailTextFields.size(); i++)
                {
                    tempPane.getChildren().removeAll(emailTextFields.get(i), emailComboBoxes.get(i));
                    if (emailTextFields.get(i).getLayoutY() == newButton.getLayoutY())
                    {
                        unluckyNode = i;
                    }
                }

                //delete the unlucky node from its respective arrayList
                emailTextFields.remove(emailTextFields.get(unluckyNode));
                emailComboBoxes.remove(emailComboBoxes.get(unluckyNode));

                removeEmailButtons.remove(newButton);

                //add nodes back to the pane
                for (int i = 0; i < removeEmailButtons.size(); i++)
                {
                    removeEmailButtons.get(i).setLayoutY((emailTitleText.getY() + yIncrement) + (yIncrement * 3 * i));
                    tempPane.getChildren().add(removeEmailButtons.get(i));
                }

                for (int i = 0; i < emailTextFields.size(); i++)
                {
                    emailTextFields.get(i).setLayoutY((emailTitleText.getY() + yIncrement) + (yIncrement * i * 3));
                    emailComboBoxes.get(i).setLayoutY((emailTitleText.getY() + yIncrement) + (yIncrement * i * 3));

                    tempPane.getChildren().addAll(emailTextFields.get(i), emailComboBoxes.get(i));
                }

                if(removeEmailButtons.size() == 0)
                {
                    initialAddEmailPlusButton.setLayoutX((emailTypeComboBox.getLayoutX()) + (xIncrement * .43));
                    initialMinusEmailButton.setVisible(false);
                }
            });
            removeEmailButtons.add(newButton);

            for(int i = 0; i < removeEmailButtons.size(); i++)
            {
                tempPane.getChildren().remove(removeEmailButtons.get(i));
            }

            for(int i = 0; i < removeEmailButtons.size(); i++)
            {
                tempPane.getChildren().add(removeEmailButtons.get(i));
            }

            //shift everything below, down
            initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() + (yIncrement * 3));
            initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() + (yIncrement * 3));
            categoryTitleText.setY(categoryTitleText.getY() + (yIncrement * 3));
            categoryListView.setLayoutY(categoryListView.getLayoutY() + (yIncrement * 3));
            addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() + (yIncrement * 3));
            removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() + (yIncrement * 3));
            addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() + (yIncrement * 3));
            addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() + (yIncrement * 3));
            removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() + (yIncrement * 3));
            commentTitleText.setY(commentTitleText.getY() + (yIncrement * 3));
            commentTextArea.setLayoutY(commentTextArea.getLayoutY() + (yIncrement * 3));
            cancelButton.setLayoutY(cancelButton.getLayoutY() + (yIncrement * 3));
            addPersonButton.setLayoutY(addPersonButton.getLayoutY() + (yIncrement * 3));

            //add another row of email elements to the page
            TextField newEmailTextField = new TextField();newEmailTextField.setLayoutX(emailTitleText.getX());newEmailTextField.setLayoutY((emailTitleText.getY() + yIncrement) + (emailTextFields.size() * 3 * yIncrement));newEmailTextField.setPromptText("Email");
            emailTextFields.add(newEmailTextField);
            ComboBox<String> newEmailComboBox = new ComboBox<>(FXCollections.observableArrayList(emailTypes));newEmailComboBox.setLayoutX(emailTitleText.getX() + (xIncrement * .8));newEmailComboBox.setLayoutY((emailTitleText.getY() + yIncrement) + (emailComboBoxes.size() * 3 * yIncrement));newEmailComboBox.setMinWidth(75);newEmailComboBox.setMaxWidth(75);newEmailComboBox.getSelectionModel().selectFirst();
            emailComboBoxes.add(newEmailComboBox);

            //add them to the pane
            //first remove everything to redraw
            for(int i = 0; i < emailTextFields.size(); i++)
            {
                tempPane.getChildren().removeAll(emailTextFields.get(i), emailComboBoxes.get(i));
            }
            //second, add them back to the pane
            for(int i = 0; i < emailTextFields.size(); i++)
            {
                tempPane.getChildren().addAll(emailTextFields.get(i), emailComboBoxes.get(i));
            }

            //if there's more than one removeEmailButtons, then displace the add button to the right
            if(removeEmailButtons.size() > 0)
            {
                initialAddEmailPlusButton.setLayoutX((phoneNumberTypeComboBox.getLayoutX() + (xIncrement * .43)) + 30);
                initialMinusEmailButton.setVisible(true);
            }
        });
        initialMinusEmailButton.setOnAction(action ->
        {
            //shift everything down
            initialAddEmailPlusButton.setLayoutY(initialAddEmailPlusButton.getLayoutY() - (yIncrement * 3));
            initialMinusEmailButton.setLayoutY(initialMinusEmailButton.getLayoutY() - (yIncrement * 3));
            categoryTitleText.setY(categoryTitleText.getY() - (yIncrement * 3));
            categoryListView.setLayoutY(categoryListView.getLayoutY() - (yIncrement * 3));
            addCategoryPlusButton.setLayoutY(addCategoryPlusButton.getLayoutY() - (yIncrement * 3));
            removeCategoryMinusButton.setLayoutY(removeCategoryMinusButton.getLayoutY() - (yIncrement * 3));
            addCategoryButton.setLayoutY(addCategoryButton.getLayoutY() - (yIncrement * 3));
            addCategoryTextField.setLayoutY(addCategoryTextField.getLayoutY() - (yIncrement * 3));
            removeCategoryButton.setLayoutY(removeCategoryButton.getLayoutY() - (yIncrement * 3));
            commentTitleText.setY(commentTitleText.getY() - (yIncrement * 3));
            commentTextArea.setLayoutY(commentTextArea.getLayoutY() - (yIncrement * 3));
            cancelButton.setLayoutY(cancelButton.getLayoutY() - (yIncrement * 3));
            addPersonButton.setLayoutY(addPersonButton.getLayoutY() - (yIncrement * 3));

            //redraw
            for(int i = 0; i < removeEmailButtons.size(); i++)
            {
                tempPane.getChildren().remove(removeEmailButtons.get(i));
            }
            for(int i = 0; i < emailTextFields.size(); i++)
            {
                tempPane.getChildren().removeAll(emailComboBoxes.get(i), emailTextFields.get(i));
            }

            //remove nodes from their respective Arraylists
            removeEmailButtons.remove(removeEmailButtons.size() - 1);
            emailTextFields.remove(emailTextFields.size() - 1);
            emailComboBoxes.remove(emailComboBoxes.size() - 1);

            //reAdd it to the pane
            for(int i = 0; i < removeEmailButtons.size(); i++)
            {
                removeEmailButtons.get(i).setLayoutY((emailTitleText.getY() + yIncrement) + (yIncrement * 3 * i)); //reset the Y value
                tempPane.getChildren().add(removeEmailButtons.get(i));
            }

            for(int i = 0; i < emailTextFields.size(); i++)
            {
                emailTextFields.get(i).setLayoutY((emailTitleText.getY() + yIncrement) + (yIncrement * i * 3));
                emailComboBoxes.get(i).setLayoutY((emailTitleText.getY() + yIncrement) + (yIncrement * i * 3));

                tempPane.getChildren().addAll(emailTextFields.get(i), emailComboBoxes.get(i));
            }

            if(removeEmailButtons.size() == 0)
            {
                initialMinusEmailButton.setVisible(false);
                initialAddEmailPlusButton.setLayoutX((emailTypeComboBox.getLayoutX()) + (xIncrement * .43));
            }
        });

        addCategoryPlusButton.setOnAction(action ->
        {
            addCategoryPlusButton.setVisible(false);
            addCategoryButton.setVisible(true);
            addCategoryTextField.setVisible(true);
            removeCategoryMinusButton.setVisible(true);
            removeCategoryButton.setVisible(false);
            categoryListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        });
        removeCategoryMinusButton.setOnAction(action ->
        {
            addCategoryButton.setVisible(false);
            addCategoryTextField.setVisible(false);
            removeCategoryMinusButton.setVisible(false);
            addCategoryPlusButton.setVisible(true);
            removeCategoryButton.setVisible(true);
            categoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        });
        addCategoryButton.setOnAction(action ->
        {
            //Button variables
            boolean valuePresent = false;


            for(int i = 0; i < this.currentUser.getCategories().size(); i++) //search through the User's list of categories.
            {
                if(this.currentUser.getCategories().get(i).equals(addCategoryTextField.getText())) //If they already have a category under that name, send an error
                {
                    valuePresent = true;
                    break;
                }
                else //otherwise, add it to the list of categories & listView
                {
                    valuePresent = false;
                }
            }

            if(!valuePresent) //If the new value is unique, add it to the currentUser, listView, & .txt file
            {
                this.currentUser.addCategory(addCategoryTextField.getText());
                categoryListView.getItems().add(addCategoryTextField.getText());

                ArrayList<String> fileContents = new ArrayList<>();
                File readFile;
                BufferedReader br;
                PrintWriter writer;
                String str;
                String concatonatedCategories = "";

                try
                {
                    readFile = new File("C:\\Creeper\\" + this.currentUser.getUsername() + "\\accountInfo.txt");
                    br = new BufferedReader(new FileReader(readFile));

                    while((str = br.readLine()) != null) //save the file contents to a ArrayList
                    {
                        fileContents.add(str);
                    }

                    br.close(); //close BufferedReader

                    writer = new PrintWriter("C:\\Creeper\\" + this.currentUser.getUsername() + "\\accountInfo.txt");

                    for(int i = 0; i < this.currentUser.getCategories().size(); i++)
                    {
                        if(i + 1 == this.currentUser.getCategories().size())
                        {
                            concatonatedCategories = concatonatedCategories + this.currentUser.getCategory(i);
                        }
                        else
                        {
                            concatonatedCategories = concatonatedCategories + this.currentUser.getCategory(i) + ">~>";
                        }

                    }

                    writer.println(fileContents.get(0)); //write out the name
                    writer.println(fileContents.get(1)); //write out the username
                    writer.println(fileContents.get(2)); //write out the encrypted password
                    writer.println(concatonatedCategories); //write out the new categories instead of the old
                    writer.close();
                } catch (Exception e)
                {
                    popUpErrorWindow("Error: " + e.toString());
                }
            }
            else
            {
                //Error message
                popUpErrorWindow("Category Already Exists.");
            }
            addCategoryTextField.clear();
        });
        removeCategoryButton.setOnAction(action ->
        {
            //button variables
            ArrayList<String> unluckyCategories = new ArrayList<>(categoryListView.getSelectionModel().getSelectedItems());
            ArrayList<String> fileContents = new ArrayList<>();
            File readFile;
            BufferedReader br;
            PrintWriter writer;
            String str;
            String concatonatedCategories = "";

            //remove it from the ListView
            final int selectedIdx = categoryListView.getSelectionModel().getSelectedIndex();
            categoryListView.getItems().remove(selectedIdx);

            //remove it from the ArrayList
            for(int i = 0; i < this.currentUser.getCategories().size(); i++)
            {
                for(int j = 0; j < unluckyCategories.size(); j++)
                {
                    if(this.currentUser.getCategories().get(i).equals(unluckyCategories.get(j)))
                    {
                        this.currentUser.removeCategory(i);
                    }
                }
            }

            //remove it from the .txt file
            try
            {
                readFile = new File("C:\\Creeper\\" + currentUser.getUsername() + "\\accountInfo.txt");
                br = new BufferedReader(new FileReader(readFile));

                while((str = br.readLine()) != null) //save the file contents to a ArrayList
                {
                    fileContents.add(str);
                }

                br.close(); //close BufferedReader

                writer = new PrintWriter("C:\\Creeper\\" + currentUser.getUsername() + "\\accountInfo.txt");

                for(int i = 0; i < this.currentUser.getCategories().size(); i++)
                {
                    if(i + 1 == this.currentUser.getCategories().size())
                    {
                        concatonatedCategories = concatonatedCategories + this.currentUser.getCategory(i);
                    }
                    else
                    {
                        concatonatedCategories = concatonatedCategories + this.currentUser.getCategory(i) + ">~>";
                    }

                }

                writer.println(fileContents.get(0)); //write out the name
                writer.println(fileContents.get(1)); //write out the username
                writer.println(fileContents.get(2)); //write out the encrypted password
                writer.println(concatonatedCategories); //write out the new categories instead of the old
                writer.println(fileContents.get(4));
                writer.close();
            }catch (Exception e)
            {
                popUpErrorWindow("Error: " + e.toString());
            }
        });

        cancelButton.setOnAction(action ->
        {
            tempStage.close();
        });

        addPersonButton.setOnAction(action ->
        {
            /* GUIDE TO THIS BUTTON:
             *--------------------------
             * Step 1: Write out a new Person to contacts.txt
             * Step 2: Check to make sure fields are valid
             */
            //local Button variables
            BufferedReader br;//BufferedReader to read in the contacts.txt file
            ArrayList<String> fileContents = new ArrayList<>();//ArrayList to store the contents of contacts.txt where each object stored in the ArrayList is its own contact
            PrintWriter writer;//PrintWriter to write out to the contacts.txt file
            String str;//a placeholder string to read in the files and save them to the ArrayList 'fileContents'
            String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"; //a regex to check for valid emails
            String phoneNumberRegex = "^\\(?\\d{3}\\)?\\s?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$";
            String dateOfBirthRegex = "\\b([1-9]|0[1-9]|1[0-2])/([1-9]|0[1-9]|[1-2]\\d|3[0-1]|)/\\d{4}$";
            String dividerOne = "<<!>>"; //The string which divides the different sections that compose a Person
            String dividerTwo = "/&/"; //The string which divides different objects from one another (i.e. Address, PhoneNumber, Email)
            String dividerThree = ">~>"; //The string which divides the aspects of the different objects apart from eachother (i.e. Street Address, city, zipcode, etc)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); //The format that the DateTime must fit

            //STEP 1:
            //take the array of address', phone numbers, & emails and save them to an arraylist to pass into the function
            ArrayList<PersonAddress> listOfAddresses = new ArrayList<>();
            for(int i = 0; i < streetAddressTextFields.size(); i++)
            {
                PersonAddress newAddress = new PersonAddress(streetAddressTextFields.get(i).getText(), cityTextFields.get(i).getText(), zipTextFields.get(i).getText(), stateComboBoxes.get(i).getSelectionModel().getSelectedItem().toString(), addressComboBoxes.get(i).getSelectionModel().getSelectedItem().toString());
                listOfAddresses.add(newAddress);
            }

            ArrayList<PersonPhoneNumber> listOfPhoneNumbers = new ArrayList<>();
            for(int i = 0; i < phoneNumberTextFields.size(); i++)
            {
                PersonPhoneNumber newPhoneNumber = new PersonPhoneNumber(phoneNumberTextFields.get(i).getText(), phoneNumberComboBoxes.get(i).getSelectionModel().getSelectedItem().toString());
                listOfPhoneNumbers.add(newPhoneNumber);
            }

            ArrayList<PersonEmail> listOfEmails = new ArrayList<>();
            for(int i = 0; i < emailTextFields.size(); i++)
            {
                PersonEmail newEmail = new PersonEmail(emailTextFields.get(i).getText(), emailComboBoxes.get(i).getSelectionModel().getSelectedItem().toString());
                listOfEmails.add(newEmail);
            }

            ArrayList<String> listOfSelectedCategories = new ArrayList<>(categoryListView.getSelectionModel().getSelectedItems());

            //STEP 2: Check everything to make sure the values are valid entries
            boolean everythingGucci = true;
            String errorMsg = "ERROR: the following values did not have valid entries...\n\n";

            //check DateTime value
            if(!dateOfBirth.getText().matches(dateOfBirthRegex))
            {
                everythingGucci = false;
                errorMsg = errorMsg + "Date of Birth is not in the right format.\nPlease use MM/DD/YYYY\n";
                dateOfBirth.setStyle("-fx-text-inner-color: red;");
            }

            //check the weightTextField
            for(int i = 0; i < weightTextField.getText().length(); i++)
            {
                if(!(((int)weightTextField.getText().charAt(i) > 47 && (int)weightTextField.getText().charAt(i) < 58) || (int)weightTextField.getText().charAt(i) == 46 || (int)weightTextField.getText().charAt(i) == 44))
                {
                    everythingGucci = false;
                    errorMsg = errorMsg + "Weight value must be a number!\n";
                    weightTextField.setStyle("-fx-text-inner-color: red;");
                }
            }
            errorMsg = errorMsg + "\n";
            //check the heightTextField
            for(int i = 0; i < heightTextField.getText().length(); i++)
            {
                if(!(((int)heightTextField.getText().charAt(i) > 47 && (int)heightTextField.getText().charAt(i) < 58) || (int)heightTextField.getText().charAt(i) == 46 || (int)heightTextField.getText().charAt(i) == 44))
                {
                    everythingGucci = false;
                    heightTextField.setStyle("-fx-text-inner-color: red;");
                    errorMsg = errorMsg + "Height value must be a number!\n";
                }
            }
            errorMsg = errorMsg + "\n";
            //check all zipcodes
            for(int i = 0; i < zipTextFields.size(); i++)
            {
                if(!(zipTextFields.get(i).getText().length() == 5 || zipTextFields.get(i).getText().length() == 0))
                {
                    everythingGucci = false;
                    errorMsg = errorMsg + "The " + (i+1) + " zipcode has an incorrect number of characters!\n";
                    zipTextFields.get(i).setStyle("-fx-text-inner-color: red;");
                }

                for(int j = 0; j < zipTextFields.get(i).getText().length(); j++)
                {
                    if(!(((int)zipTextFields.get(i).getText().charAt(j) > 47 && (int)zipTextFields.get(i).getText().charAt(j) < 58)))
                    {
                        everythingGucci = false;
                        errorMsg = errorMsg + "The " + (i+1) + " zipcode has an invalid character\n";
                        zipTextFields.get(i).setStyle("-fx-text-inner-color: red;");
                    }
                }

            }
            errorMsg = errorMsg + "\n";

            //check the phone numbers
            for(int i = 0; i < phoneNumberTextFields.size(); i++)
            {
                if(phoneNumberTextFields.get(i).getText().length() > 0)
                {
                    if (!phoneNumberTextFields.get(i).getText().matches(phoneNumberRegex))
                    {
                        everythingGucci = false;
                        errorMsg = errorMsg + "The " + (i + 1) + " phone number doesn't match!\nAssure it's in the correct format: ###-###-####\n";
                        phoneNumberTextFields.get(i).setStyle("-fx-text-inner-color: red;");
                    }
                }
            }
            errorMsg = errorMsg + "\n";
            //check the emails
            for(int i = 0; i < emailTextFields.size(); i++)
            {
                if(emailTextFields.get(i).getText().length() > 0) //make sure the email isn't blank
                {
                    if(!emailTextFields.get(i).getText().matches(emailRegex))
                    {
                        everythingGucci = false;
                        errorMsg = errorMsg + "The " + (i+1) + " email is not valid\n";
                        emailTextFields.get(i).setStyle("-fx-text-inner-color: red;");
                    }
                }
            }

            if(!everythingGucci)
            {
                popUpErrorWindow(errorMsg);

                //reset the color of the TextFields
                dateOfBirth.setStyle("-fx-text-inner-color: black");
                weightTextField.setStyle("-fx-text-inner-color: black;");
                heightTextField.setStyle("-fx-text-inner-color: black;");

                for(TextField tf : zipTextFields)
                {
                    tf.setStyle("-fx-text-inner-color: black;");
                }
                for(TextField tf : emailTextFields)
                {
                    tf.setStyle("-fx-text-inner-color: black;");
                }
                for(TextField tf : phoneNumberTextFields)
                {
                    tf.setStyle("-fx-text-inner-color: black;");
                }
            }
            else
            {
                //set the information inputted by the user to the newPerson object
                newPerson.setFirstName(firstNameTextField.getText());
                newPerson.setMiddleName(middleNameTextField.getText());
                newPerson.setLastName(lastNameTextField.getText());
                newPerson.setDateOfBirth(LocalDate.parse(dateOfBirth.getText(), formatter));
                newPerson.setWeight(weightTextField.getText());
                newPerson.setHeight(heightTextField.getText());
                newPerson.setAddresses(listOfAddresses);
                newPerson.setPhoneNumbers(listOfPhoneNumbers);
                newPerson.setEmails(listOfEmails);
                newPerson.setCategories(listOfSelectedCategories);
                newPerson.setComment(commentTextArea.getText().replaceAll("\\n", "<'r'>")); //replace the returns with `*` to correctly save the file

                //set if the Person is a male or female
                if(maleRadioButton.isSelected())
                {
                    newPerson.setSex("Male");
                }
                else if(femaleRadioButton.isSelected())
                {
                    newPerson.setSex("Female");
                }
                else
                {
                    newPerson.setSex("Empty");
                }
                //set Eye Color & Hair Color
                //Eye Color First
                if(eyecolorComboBox.getSelectionModel().getSelectedItem().equals("--"))
                {
                    newPerson.setEyeColor("Empty");
                }
                else
                {
                    newPerson.setEyeColor(eyecolorComboBox.getSelectionModel().getSelectedItem());
                }

                //and now Hair Color
                if(hairColorComboBox.getSelectionModel().getSelectedItem().equals("--"))
                {
                    newPerson.setHairColor("Empty");
                }
                else
                {
                    newPerson.setHairColor(hairColorComboBox.getSelectionModel().getSelectedItem());
                }

                //read in the current contacts.txt for future & write the new person out to the file
                try
                {
                    //read in the text and save it locally before overwriting it with the new information
                    br = new BufferedReader(new FileReader(filePath));//set the path to read in from

                    while((str = br.readLine()) != null)
                    {
                        fileContents.add(str);
                    }

                    br.close();//don't need this anymore
                    writer = new PrintWriter(filePath);//do need this now

                    for(int i = 0; i < fileContents.size(); i++)//loops through fileContents and writes it out to the file
                    {
                        writer.println(fileContents.get(i));
                    }

                    //concatenate string to contain all the information on the new Person
                    String personInformation = "";

                    personInformation = personInformation + newPerson.getLastName() + dividerThree
                            + newPerson.getFirstName() + dividerThree
                            + newPerson.getMiddleName() + dividerThree
                            + newPerson.getDateOfBirth().toString() + dividerThree
                            + newPerson.getSex() + dividerThree
                            + newPerson.getWeight() + dividerThree
                            + newPerson.getHeight() + dividerThree
                            + newPerson.getEyeColor() + dividerThree
                            + newPerson.getHairColor() + dividerThree
                            + newPerson.getComment() + dividerOne;

                    for(int i = 0; i < newPerson.getAddresses().size(); i++)
                    {
                        if(i + 1 == newPerson.getAddresses().size())
                        {
                            personInformation = personInformation + newPerson.getAddresses().get(i).getStreetAddress() + dividerThree
                                    + newPerson.getAddresses().get(i).getCity() + dividerThree
                                    + newPerson.getAddresses().get(i).getZipcode() + dividerThree
                                    + newPerson.getAddresses().get(i).getState() + dividerThree
                                    + newPerson.getAddresses().get(i).getType() + dividerOne;
                        }
                        else
                        {
                            personInformation = personInformation + newPerson.getAddresses().get(i).getStreetAddress() + dividerThree
                                    + newPerson.getAddresses().get(i).getCity() + dividerThree
                                    + newPerson.getAddresses().get(i).getZipcode() + dividerThree
                                    + newPerson.getAddresses().get(i).getState() + dividerThree
                                    + newPerson.getAddresses().get(i).getType() + dividerTwo;
                        }
                    }
                    for(int i = 0; i < newPerson.getPhoneNumbers().size(); i++)
                    {
                        if(i + 1 == newPerson.getPhoneNumbers().size())
                        {
                            personInformation = personInformation + newPerson.getPhoneNumbers().get(i).getPhoneNumber() + dividerThree
                                    + newPerson.getPhoneNumbers().get(i).getType() + dividerOne;
                        }
                        else
                        {
                            personInformation = personInformation + newPerson.getPhoneNumbers().get(i).getPhoneNumber() + dividerThree
                                    + newPerson.getPhoneNumbers().get(i).getType() + dividerTwo;
                        }
                    }
                    for(int i = 0; i < newPerson.getEmails().size(); i++)
                    {
                        if(i + 1 == newPerson.getEmails().size())
                        {
                            personInformation = personInformation + newPerson.getEmails().get(i).getEmailAddress() + dividerThree
                                    + newPerson.getEmails().get(i).getType() + dividerOne;
                        }
                        else
                        {
                            personInformation = personInformation + newPerson.getEmails().get(i).getEmailAddress() + dividerThree
                                    + newPerson.getEmails().get(i).getType() + dividerTwo;
                        }
                    }
                    for(int i = 0; i < newPerson.getCategories().size(); i++)
                    {
                        if(i + 1 == newPerson.getCategories().size())
                        {
                            personInformation = personInformation + newPerson.getCategories().get(i);
                        }
                        else
                        {
                            personInformation = personInformation + newPerson.getCategories().get(i) + dividerThree;
                        }
                    }

                    writer.println(personInformation); //write out all information on the new person on the next line in contacts.txt
                    writer.close();//don't need this either
                }catch(Exception e)
                {
                    popUpErrorWindow("Error: " + e.toString());
                }

                tempStage.close();
            }
;
        });

        tempPane.getChildren().addAll(nameTitleText, dateOfBirthTitleText, sexTitleText, eyeColorTitleText, hairColorTitleText, weightTitleText, weightHelperText, heightTitleText, heightHelperText, commentTitleText, addressTitleText, phoneNumberTitleText, emailTitleText, categoryTitleText, firstNameTextField, middleNameTextField, lastNameTextField, dateOfBirth, heightTextField, weightTextField, eyecolorComboBox, hairColorComboBox, addCategoryTextField, commentTextArea, cancelButton, addPersonButton, removeCategoryMinusButton, addCategoryPlusButton, addCategoryButton, removeCategoryButton, initialMinusAddressButton, initialAddAddressPlusButton, initialMinusPhoneNumberButton, initialAddPhoneNumberPlusButton, initialMinusEmailButton, initialAddEmailPlusButton, categoryListView, phoneNumberTypeComboBox, emailTypeComboBox, maleRadioButton, femaleRadioButton);
        Scene tempScene = new Scene(sp);
        tempStage.setScene(tempScene);
        tempStage.showAndWait();
        return newPerson;
    }

    private void popUpErrorWindow(String l_error) //a pop up window to display any errors the user may encounter
    {
        Stage tempStage = new Stage();tempStage.initModality(Modality.APPLICATION_MODAL);tempStage.setTitle("Error Message");tempStage.setWidth(400);tempStage.setHeight(250);tempStage.setResizable(false);
        Pane tempPane = new Pane();

        Text errorception = new Text();
        try //set the icon image
        {
            tempStage.getIcons().add(new Image("https://c8.alamy.com/comp/J0MWT6/error-message-icon-with-exclamation-mark-J0MWT6.jpg"));
        }catch(Exception e)
        {
            errorception.setText("(local) ERROR: " + e.toString());errorception.setX((tempStage.getWidth() / 2) - (errorception.getLayoutBounds().getWidth() / 2));errorception.setY((tempStage.getHeight() / 2));errorception.setFill(Color.YELLOW);
        }

        Text errorText = new Text();errorText.setText(l_error);errorText.setX((tempStage.getWidth() / 2) - (errorText.getLayoutBounds().getWidth() / 2));errorText.setY(tempStage.getHeight() - 230);errorText.setFill(Color.RED);
        tempStage.setWidth(errorText.getLayoutBounds().getWidth() + 100);
        tempStage.setHeight(errorText.getLayoutBounds().getHeight() + 150);

        Button acknowledgeButton = new Button("OK");acknowledgeButton.setPrefWidth(50);acknowledgeButton.setPrefHeight(30);acknowledgeButton.setLayoutX((tempStage.getWidth() / 2) - (acknowledgeButton.getPrefWidth()));acknowledgeButton.setLayoutY(tempStage.getHeight() - acknowledgeButton.getPrefHeight() - 50);acknowledgeButton.setMinHeight(40);acknowledgeButton.setMinWidth(80);
        acknowledgeButton.setOnAction(action ->
        {
            tempStage.close();
        });

        tempPane.getChildren().addAll(errorText, errorception, acknowledgeButton);
        Scene tempScene = new Scene(tempPane);
        tempStage.setScene(tempScene);
        tempStage.showAndWait();
    }
}