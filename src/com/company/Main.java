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
 */

package com.company;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main extends Application
{
    private ObservableList<Person> contacts = FXCollections.observableArrayList();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private User currentUser = new User();
    private Pane pane = new Pane();
    private boolean hasBeenClicked;
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage tempStage)
    {
        tempStage.setTitle("Creeper");tempStage.setWidth(800);tempStage.setHeight(400);tempStage.setResizable(false);
        Pane tempPane = new Pane();tempPane.setStyle("-fx-background-color: white");

        try //set the icon image
        {
            tempStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            System.out.println("ERROR: " + e);
        }

        //Text Objects in the log in screen
        Text logInWelcomeText = new Text("Log In");logInWelcomeText.setX((tempStage.getWidth() / 2) - logInWelcomeText.getLayoutBounds().getWidth());logInWelcomeText.setY(tempStage.getHeight() / 4);logInWelcomeText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 26));
        Text noAccountFlavorText = new Text("Don't have an account?");noAccountFlavorText.setX(((tempStage.getWidth() / 2) - noAccountFlavorText.getLayoutBounds().getWidth() / 2) - 25);noAccountFlavorText.setY(tempStage.getHeight() - 70);noAccountFlavorText.setFont(Font.font("Times New Roman", 13));
        Text helpText = new Text();helpText.setY((tempStage.getHeight() / 3) + 160);helpText.setX((tempStage.getWidth() / 2) - (helpText.getLayoutBounds().getWidth() / 2));
        Hyperlink makeAccountText = new Hyperlink("Click Here");makeAccountText.setLayoutX(noAccountFlavorText.getX() + noAccountFlavorText.getLayoutBounds().getWidth());makeAccountText.setLayoutY(noAccountFlavorText.getY() - noAccountFlavorText.getLayoutBounds().getHeight() - 2);makeAccountText.setFont(Font.font("Times New Roman", FontWeight.THIN, 13));

        makeAccountText.setOnAction(action ->
        {
            makeAccountScreen();
        });

        //TextField's
        TextField userNameTextField = new TextField();userNameTextField.setPromptText("Username");userNameTextField.setLayoutX((tempStage.getWidth() / 2) - 70);userNameTextField.setLayoutY(tempStage.getHeight() / 3);
        PasswordField passwordTextField = new PasswordField();passwordTextField.setPromptText("Password");passwordTextField.setLayoutX((tempStage.getWidth() / 2) - 70);passwordTextField.setLayoutY(userNameTextField.getLayoutY() + 50);

        //Buttons in log in screen
        Button verifyButton = new Button("Verify");verifyButton.setLayoutX((tempStage.getWidth() / 2) - 40);verifyButton.setLayoutY(passwordTextField.getLayoutY() + 50);verifyButton.setMinWidth(70);verifyButton.setMinHeight(30);verifyButton.setDefaultButton(true);

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
                helpText.setText("Username or Password incorrect");helpText.setX((tempStage.getWidth() / 2) - (helpText.getLayoutBounds().getWidth() / 2));helpText.setFill(Color.RED);
            }
        });

        tempPane.getChildren().addAll(logInWelcomeText, helpText, userNameTextField, passwordTextField, verifyButton, noAccountFlavorText, makeAccountText);
        Scene tempScene = new Scene(tempPane);
        tempStage.setScene(tempScene);
        tempStage.show();
    }

    private void makeAccountScreen() //opens a window allowing user to create an account with a fresh set of contacts
    {
        hasBeenClicked = false;

        //make and launch new stage
        Stage tempStage = new Stage();tempStage.setWidth(400);tempStage.setHeight(220);tempStage.initModality(Modality.APPLICATION_MODAL);tempStage.setTitle("Account Creation");tempStage.setResizable(false);
        Pane tempPane = new Pane();tempPane.setStyle("-fx-background-color: white");


        try //set the icon image
        {
            tempStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            System.out.println("ERROR: " + e);
        }

        //Text Objects
        Text informationalText = new Text("Personal Details");informationalText.setX(tempStage.getWidth() - (tempStage.getWidth() * 0.98));informationalText.setY(tempStage.getHeight() - (tempStage.getHeight() * 0.90));informationalText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        Text helpText = new Text();helpText.setX(informationalText.getX());helpText.setY(informationalText.getY() + 20);helpText.setFont(Font.font("Verdana", 12));

        //TextField Objects
        TextField firstNameTextField = new TextField();firstNameTextField.setLayoutX(tempStage.getWidth() - (tempStage.getWidth() * 0.98));firstNameTextField.setLayoutY(informationalText.getY() + 30);firstNameTextField.setPromptText("First Name");firstNameTextField.setVisible(true);
        TextField lastNameTextField = new TextField();lastNameTextField.setLayoutX(tempStage.getWidth() - (tempStage.getWidth() * 0.98));lastNameTextField.setLayoutY(firstNameTextField.getLayoutY() + 30);lastNameTextField.setPromptText("Last Name");lastNameTextField.setVisible(true);
        TextField userNameTextField = new TextField();userNameTextField.setLayoutX(informationalText.getX());userNameTextField.setLayoutY(firstNameTextField.getLayoutY());userNameTextField.setPromptText("Username");userNameTextField.setVisible(false);

        //passwordField Object(s)
        TextField passwordTextField = new TextField();passwordTextField.setLayoutX(informationalText.getX());passwordTextField.setLayoutY(userNameTextField.getLayoutY() + 30);passwordTextField.setPromptText("Password");passwordTextField.setVisible(false);

        //Images
        Image arrowImage = new Image("https://cdn3.iconfinder.com/data/icons/ui-elements-light/100/UI_Light_chevron_right-512.png");
        ImageView arrowImageView = new ImageView(arrowImage);arrowImageView.setFitHeight(100);arrowImageView.setFitWidth(40);

        //Buttons
        Button continueButton = new Button("", arrowImageView);continueButton.setMinWidth(40);continueButton.setMinHeight(200);continueButton.setLayoutX(tempStage.getWidth() - 60);continueButton.setLayoutY((tempStage.getHeight() / 2) - (continueButton.getMinHeight() / 1.5));continueButton.setStyle("-fx-background-color: white;");continueButton.setStyle("-fx-border-color: black;");

        //button action
        continueButton.setOnAction(action ->
        {
            if(!this.hasBeenClicked) //logic for the first time the button has been clicked
            {
                //save info from name text Fields
                if(!firstNameTextField.getText().equals("") && !lastNameTextField.getText().equals("")) //logic to prevent empty text fields
                {
                    //switch out textFields
                    firstNameTextField.setVisible(false);
                    lastNameTextField.setVisible(false);
                    userNameTextField.setVisible(true);
                    passwordTextField.setVisible(true);

                    //change informationalText
                    informationalText.setText("Account Details");

                    //change continueButton location & visuals
                    continueButton.setText("Finish"); //set text
                    continueButton.setGraphic(null); //get rid of image
                    continueButton.setLayoutX(passwordTextField.getLayoutX());
                    continueButton.setLayoutY(passwordTextField.getLayoutY() + 30);
                    continueButton.setMinWidth(60);continueButton.setMaxWidth(60);
                    continueButton.setMinHeight(30);continueButton.setMaxHeight(30);

                    //reset variables
                    helpText.setText("");
                    this.hasBeenClicked = true;
                }else { //tell the user they're retarded

                    helpText.setText("You cannot leave a Text Field blank!");
                }

            }else //logic for the second time the button has been clicked
            {
                if(!userNameTextField.getText().equals("") && !passwordTextField.getText().equals(""))
                {
                    //make the folders & .txt files to store information about the user
                    createDirectories(firstNameTextField.getText(), lastNameTextField.getText(), userNameTextField.getText(), passwordTextField.getText());

                    //reset the hasBeenClicked to false
                    this.hasBeenClicked = false;
                    tempStage.close();
                }else //tell the user they're retarded
                {
                    helpText.setText("You cannot leave a Text Field blank!");
                }
            }
        });

        tempPane.getChildren().addAll(informationalText, helpText, firstNameTextField, lastNameTextField, userNameTextField, passwordTextField, continueButton);
        Scene tempScene = new Scene(tempPane);
        tempStage.setScene(tempScene);
        tempStage.show();
    }

    private void createDirectories(String l_firstName, String l_lastName, String l_username, String l_password) //makes folders & .txt folders for new user accounts as well as adds that user to the users ObservableArrayList
    {
        //local variables
        String status = "";
        //add new user to the list
        User newUser = new User(l_firstName, l_lastName, l_username, l_password);

        //Make folders
        File folders = new File("C:\\Creeper\\" + l_username);
        try
        {
            if(!folders.exists())
            {
                if(folders.mkdirs())
                {
                    status = status + "Directory creation successful!\n";
                }
                else
                {
                    status = status + "Failure to create Directory\n";
                }
            }
            else
            {
                status = status + "Directory already exists\n";
            }
        }catch(Exception e)
        {
            System.out.println("ERROR: " + e.toString());
        }

        //make .txt files
        File accountInfoTextFile = new File("C:\\Creeper\\" + l_username + "\\accountInfo.txt");
        File contactsInfoTextFile = new File("C:\\Creeper\\" + l_username + "\\contacts.txt");

        try
        {
            if(accountInfoTextFile.createNewFile() && contactsInfoTextFile.createNewFile())
            {
                status = status + "Text File creation successful!\n";
            }
            else
            {
                status = status + "An error occurred in creating the .txt files\n";
            }
        }catch (Exception e)
        {
            System.out.println("ERROR: " + e.toString());
        }

        //write out the account info to accountInfo.txt
        try
        {
            PrintWriter writer = new PrintWriter("C:\\Creeper\\" + l_username + "\\accountInfo.txt");
            writer.println(newUser.getFirstName() + " " + newUser.getLastName()); //Write out to the .txt file "John Doe"
            writer.println(newUser.getUsername()); //on a new line, write out the username
            writer.println(newUser.getEncryptedPassword()); //on a new line, write out the encrypted password
            writer.close();
        }catch (Exception e)
        {
            System.out.println("Error: " + e.toString());
        }

        System.out.println(status);
    }

    private boolean verifyAccount(String l_username, String l_passwordAttempt) //verifies username & password as well as reads in all account information and contacts information
    {
        //local variables
        User addUser;//a User object to create a new User
        Person readInPerson;//a Person object to represent the current Person being read in and saved to the global contacts ObservableList
        File readFile = new File("C:\\Creeper\\" + l_username); //File data type to store the file path to the pertinent information
        BufferedReader br;//the BufferedReader used to read in the .txt files
        ArrayList<String> fileContents = new ArrayList<>(); //to store the fileContents of both accountInfo.txt & contacts.txt at different times
        String[] contactDetails; //to store the regex results of the contents of contacts.txt
        String[] splitFullName;//get the first & last name by splitting the full Name up using a white space
        String firstName, lastName, fullName; //Strings to store the individual first, last, and full name of that found in accountInfo.txt
        String username, encryptedPasswordReadIn, encryptedPasswordInput; //Strings to store the username and password read in as found in accountInfo.txt.  Also contains the encryptedPassword that the user types in and is verified through encryption
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

                fullName = fileContents.get(0);splitFullName = fullName.split(" ");firstName = splitFullName[0];lastName = splitFullName[1];
                username = fileContents.get(1);
                encryptedPasswordReadIn = fileContents.get(2);
                addUser = new User();
                addUser.setFirstName(firstName);
                addUser.setLastName(lastName);
                addUser.setUsername(username);
                encryptedPasswordInput = addUser.hash(l_passwordAttempt);

                this.users.add(addUser); //add the user to the global ArrayList
                this.currentUser = addUser; //make the current user equal to who they logged in to

                br.close();
                //TODO: read in contacts.txt and save it to my ArrayList
                readFile = new File("C:\\Creeper\\" + l_username + "\\contacts.txt");
                br = new BufferedReader(new FileReader(readFile));

                str = "";
                fileContents.clear();
                while((str = br.readLine()) != null) //read in the contents of the file & add them to an ArrayList<String> where each item in the list represents a Person object
                {
                    fileContents.add(str);
                }

                for(int i = 0; i < fileContents.size(); i++) //goes through each line of contacts.txt and splits them apart via regex
                {
                    readInPerson = new Person();
                    System.out.println(fileContents.get(i));
                    contactDetails = fileContents.get(i).split(">~>", -1);
                    readInPerson.setLastName(contactDetails[0]);
                    readInPerson.setFirstName(contactDetails[1]);
                    readInPerson.setMiddleName(contactDetails[2]);
                    //readInPerson.setDateOfBirth(contactDetails[3]);//TODO:Figure out String -> Date class
                    readInPerson.setPrimaryAddress(contactDetails[4]);
                    readInPerson.setSecondaryAddress(contactDetails[5]);
                    readInPerson.setHomePhoneNumber(contactDetails[6]);
                    readInPerson.setCellPhoneNumber(contactDetails[7]);
                    readInPerson.setPersonalEmail(contactDetails[8]);
                    readInPerson.setWorkEmail(contactDetails[9]);

                    contacts.add(readInPerson);
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
            System.out.println("ERROR: " + e);
            return false;
        }
    }

    private void mainScreen() //the home page
    {
        //local variables
        ObservableList<Person> recentlySearchedPeople = FXCollections.observableArrayList();

        //set all the defaults for the stage
        Stage primaryStage = new Stage();primaryStage.setTitle("Creeper");primaryStage.setWidth(900);primaryStage.setHeight(650);primaryStage.setResizable(false);
        try //set the icon image
        {
            primaryStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            System.out.println("ERROR: " + e);
        }

        //declare nodes on screen
        //Text
        Text nameText = new Text("[Name]");nameText.setX(primaryStage.getWidth() - 890);nameText.setY(primaryStage.getHeight() - 630);nameText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        Text DoBText = new Text("[DoB]");DoBText.setX(primaryStage.getWidth() - 890);DoBText.setY(primaryStage.getHeight() - 600);DoBText.setFont(Font.font("Times New Roman", 14));
        Text addressText = new Text("[Address]");addressText.setX(primaryStage.getWidth() - 890);addressText.setY(primaryStage.getHeight() - 550);addressText.setFont(Font.font("Times New Roman", 14));
        Text phoneNumberText = new Text("[Phone #]");phoneNumberText.setX(primaryStage.getWidth() - 890);phoneNumberText.setY(primaryStage.getHeight() - 500);phoneNumberText.setFont(Font.font("Times New Roman", 14));
        Text moreDetailsText = new Text("More Details...");moreDetailsText.setX(primaryStage.getWidth() - 890);moreDetailsText.setY(primaryStage.getHeight() - 450);moreDetailsText.setUnderline(true);moreDetailsText.setFill(Color.BLUE);

        //listView
        ListView<Person> recentlySearchedListView = new ListView<>();recentlySearchedListView.setMinWidth(170);recentlySearchedListView.setMaxWidth(170);recentlySearchedListView.setMinHeight(380);recentlySearchedListView.setMaxHeight(380);recentlySearchedListView.setLayoutX(primaryStage.getWidth() - 200);recentlySearchedListView.setLayoutY(primaryStage.getHeight() - 520);

        //buttons
        Button exitButton = new Button("Exit");exitButton.setMinHeight(50);exitButton.setMinWidth(100);exitButton.setLayoutX(primaryStage.getWidth() - 130); exitButton.setLayoutY(primaryStage.getHeight() - 120);
        Button addPersonButton = new Button("Add Person");addPersonButton.setMinHeight(40);addPersonButton.setMinWidth(100);addPersonButton.setLayoutX(primaryStage.getWidth() - 130);addPersonButton.setLayoutY(primaryStage.getHeight() - 630);
        Button searchExistingButton = new Button("Search Existing");searchExistingButton.setMinHeight(40);searchExistingButton.setMinWidth(100);searchExistingButton.setLayoutX(primaryStage.getWidth() - 130);searchExistingButton.setLayoutY(primaryStage.getHeight() - 580);searchExistingButton.setMaxWidth(100);searchExistingButton.setStyle("-fx-font-size:11");

        //node Functions
        //listView
        for(int i = 0; i < recentlySearchedPeople.size(); i++)
        {
            recentlySearchedListView.getItems().add(recentlySearchedPeople.get(i)); //TODO: Use your new knowledge of data structures to figure out how to put the most recent search at the top
        }

        //buttons
        exitButton.setOnAction(action ->
        {
            System.exit(0);
        });

        addPersonButton.setOnAction(action ->
        {
            Person tempPerson = addPersonWindow();
            contacts.add(tempPerson);
        });

        searchExistingButton.setOnAction(action -> //TODO: Figure out functionality for search button
        {
            //wipe info from labels
            nameText.setText("");DoBText.setText("");addressText.setText("");phoneNumberText.setText("");
            Person selectedPerson = searchPersonWindow();
            if(selectedPerson == null)
            {
                nameText.setText("Error: No Contact was Selected...");
                DoBText.setText("Error: No Contact was Selected...");
                addressText.setText("Error: No Contact was Selected...");
                phoneNumberText.setText("Error: No Contact was Selected...");
            }
            else
            {
                recentlySearchedListView.getItems().add(selectedPerson);
                nameText.setText(selectedPerson.toString());
                //DoBText.setText(selectedPerson.getDateOfBirth().toString());
                addressText.setText(selectedPerson.getPrimaryAddress());
                phoneNumberText.setText(selectedPerson.getCellPhoneNumber());
            }
        });

        //add all nodes to pane
        this.pane.getChildren().addAll(nameText, DoBText, addressText, phoneNumberText, moreDetailsText, recentlySearchedListView, exitButton, addPersonButton, searchExistingButton);
        Scene scene = new Scene(this.pane);
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
            System.out.println("ERROR: " + e);
        }

        //make all objects in searchPersonWindow
        //Text objects
        Text advancedSearch = new Text("more advanced search");advancedSearch.setX(tempStage.getWidth() - 545);advancedSearch.setY(tempStage.getHeight() - 50);

        //TextBox's
        TextField nameTextField = new TextField("Last, First Middle Name");nameTextField.setLayoutX(tempStage.getWidth() - 545);nameTextField.setLayoutY(tempStage.getHeight() - 580);
        TextField phoneNumberTextField = new TextField("Phone Number");phoneNumberTextField.setLayoutX(tempStage.getWidth() - 357.5);phoneNumberTextField.setLayoutY(tempStage.getHeight() - 580);
        TextField addressTextField = new TextField("Address");addressTextField.setLayoutX(tempStage.getWidth() - 170);addressTextField.setLayoutY(tempStage.getHeight() - 580);

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
                String currentFullName = this.contacts.get(i).getFirstName() + this.contacts.get(i).getMiddleName() + this.contacts.get(i).getLastName(); //TODO: Make the currentFullName all lowercase as well as all the tempNames lowercase as well for more accurate searching
                if(currentFullName.contains(tempLastName) && !tempLastName.equals(""))
                {
                    searchBox.getItems().add(listBoxIndex, this.contacts.get(i));//this.contacts.get(i).getLastName() + ", " + this.contacts.get(i).getFirstName() + " " + this.contacts.get(i).getMiddleName());
                }
                if(currentFullName.contains(tempFirstName) && !tempFirstName.equals(""))
                {
                    if(!searchBox.getItems().contains(this.contacts.get(i))) //tests to see if the person has already been added
                    {
                        searchBox.getItems().add(listBoxIndex, this.contacts.get(i));// this.contacts.get(i).getLastName() + ", " + this.contacts.get(i).getFirstName() + " " + this.contacts.get(i).getMiddleName());
                    }
                }
                if(currentFullName.contains(tempMiddleName) && !tempMiddleName.equals(""))
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
                if(this.contacts.get(i).getCellPhoneNumber().contains(tempPhoneNumber) && !tempPhoneNumber.equals(""))
                {
                    if(!searchBox.getItems().contains(this.contacts.get(i)))
                    {
                        searchBox.getItems().add(listBoxIndex, this.contacts.get(i));
                    }
                }
                else if (this.contacts.get(i).getHomePhoneNumber().contains(tempPhoneNumber) && !tempPhoneNumber.equals(""))
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

            for(int i = 0; i < contacts.size(); i++)
            {
                if(this.contacts.get(i).getPrimaryAddress().contains(tempAddress) && !tempAddress.equals(""))
                {
                    if(!searchBox.getItems().contains(this.contacts.get(i)))
                    {
                        searchBox.getItems().add(listBoxIndex, this.contacts.get(i));
                    }
                }
                if(this.contacts.get(i).getSecondaryAddress().contains(tempAddress) && !tempAddress.equals(""))
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
        Person newPerson = new Person(); //Person object to save to contacts.txt
        final File filePath = new File("C:\\Creeper\\" + this.currentUser.getUsername() + "\\contacts.txt");//File Path...Won't change

        //make a new stage and pane
        Stage tempStage = new Stage();tempStage.initModality(Modality.APPLICATION_MODAL);tempStage.setTitle("Add Contact");tempStage.setWidth(550);tempStage.setHeight(600);tempStage.setResizable(false); //details for the new window
        Pane tempPane = new Pane();

        try //set the icon image
        {
            tempStage.getIcons().add(new Image("https://i.pinimg.com/originals/7e/67/79/7e6779bf6d689ef9d288052bdbfdcf41.jpg"));
        }catch(Exception e)
        {
            System.out.println("ERROR: " + e.toString());
        }

        //make all nodes in add person window
        //Text Objects
        int yInitial = 580;
        int xIncrement = 540;
        int yIncrement = 35;
        Text firstNamePrompt = new Text("First Name:");firstNamePrompt.setX(tempStage.getWidth() - xIncrement);firstNamePrompt.setY(tempStage.getHeight() - yInitial);
        Text middleNamePrompt = new Text("Middle Name:");middleNamePrompt.setX(tempStage.getWidth() - xIncrement);middleNamePrompt.setY(tempStage.getHeight() - yInitial + (yIncrement));
        Text lastNamePrompt = new Text("Last Name:");lastNamePrompt.setX(tempStage.getWidth() - xIncrement);lastNamePrompt.setY(tempStage.getHeight() - yInitial + (yIncrement * 2));
        Text dateOfBirthPrompt = new Text("Date of Birth:");dateOfBirthPrompt.setX(tempStage.getWidth() - xIncrement);dateOfBirthPrompt.setY(tempStage.getHeight() - yInitial + (yIncrement * 3));
        Text primaryAddressPrompt = new Text("Primary Address:");primaryAddressPrompt.setX(tempStage.getWidth() - xIncrement);primaryAddressPrompt.setY(tempStage.getHeight() - yInitial + (yIncrement * 4));
        Text secondaryAddressPrompt = new Text("Secondary Address:");secondaryAddressPrompt.setX(tempStage.getWidth() - xIncrement);secondaryAddressPrompt.setY(tempStage.getHeight() - yInitial + (yIncrement * 5));
        Text homePhoneNumberPrompt = new Text("Home Phone Number:");homePhoneNumberPrompt.setX(tempStage.getWidth() - xIncrement);homePhoneNumberPrompt.setY(tempStage.getHeight() - yInitial + (yIncrement * 6));
        Text cellPhoneNumberPrompt = new Text("Cell Phone Number:");cellPhoneNumberPrompt.setX(tempStage.getWidth() - xIncrement);cellPhoneNumberPrompt.setY(tempStage.getHeight() - yInitial + (yIncrement * 7));
        Text personalEmailPrompt = new Text("Personal Email:");personalEmailPrompt.setX(tempStage.getWidth() - xIncrement);personalEmailPrompt.setY(tempStage.getHeight() - yInitial + (yIncrement * 8));
        Text workEmailPrompt = new Text("Work Email:");workEmailPrompt.setX(tempStage.getWidth() - xIncrement);workEmailPrompt.setY(tempStage.getHeight() - yInitial + (yIncrement * 9));//***COULD BE PART OF THE EMPLOYMENT CLASS IN PLACE OF PERSON CLASS***

        //TextBox Objects
        xIncrement = 130;
        yIncrement = 15;
        TextField firstNameTextField = new TextField();firstNameTextField.setLayoutX(firstNamePrompt.getX() + xIncrement);firstNameTextField.setLayoutY(firstNamePrompt.getY() - yIncrement);
        TextField middleNameTextField = new TextField();middleNameTextField.setLayoutX(middleNamePrompt.getX() + xIncrement);middleNameTextField.setLayoutY(middleNamePrompt.getY() - yIncrement);
        TextField lastNameTextField = new TextField();lastNameTextField.setLayoutX(lastNamePrompt.getX() + xIncrement);lastNameTextField.setLayoutY(lastNamePrompt.getY() - yIncrement);
        TextField dateOfBirthTextField = new TextField();dateOfBirthTextField.setLayoutX(dateOfBirthPrompt.getX() + xIncrement);dateOfBirthTextField.setLayoutY(dateOfBirthPrompt.getY() - yIncrement);
        TextField primaryAddressTextField = new TextField();primaryAddressTextField.setLayoutX(primaryAddressPrompt.getX() + xIncrement);primaryAddressTextField.setLayoutY(primaryAddressPrompt.getY() - yIncrement);
        TextField secondaryAddressTextField = new TextField();secondaryAddressTextField.setLayoutX(secondaryAddressPrompt.getX() + xIncrement);secondaryAddressTextField.setLayoutY(secondaryAddressPrompt.getY() - yIncrement);
        TextField homePhoneNumberTextField = new TextField();homePhoneNumberTextField.setLayoutX(homePhoneNumberPrompt.getX() + xIncrement);homePhoneNumberTextField.setLayoutY(homePhoneNumberPrompt.getY() - yIncrement);
        TextField cellPhoneNumberTextField = new TextField();cellPhoneNumberTextField.setLayoutX(cellPhoneNumberPrompt.getX() + xIncrement);cellPhoneNumberTextField.setLayoutY(cellPhoneNumberPrompt.getY() - yIncrement);
        TextField personalEmailTextField = new TextField();personalEmailTextField.setLayoutX(personalEmailPrompt.getX() + xIncrement);personalEmailTextField.setLayoutY(personalEmailPrompt.getY() - yIncrement);
        TextField workEmailTextField = new TextField();workEmailTextField.setLayoutX(workEmailPrompt.getX() + xIncrement);workEmailTextField.setLayoutY(workEmailPrompt.getY() - yIncrement);

        //Buttons
        Button cancelButton = new Button("Cancel");cancelButton.setMinHeight(50);cancelButton.setMinWidth(100);cancelButton.setLayoutX(tempStage.getWidth() - 130); cancelButton.setLayoutY(tempStage.getHeight() - 120);
        Button addPersonButton = new Button("Add Information");addPersonButton.setMinHeight(50);addPersonButton.setMinWidth(100);addPersonButton.setLayoutX(tempStage.getWidth() - 540);addPersonButton.setLayoutY(tempStage.getHeight() - 120);

        //Node Functionality
        //Buttons
        cancelButton.setOnAction(action ->
        {
            tempStage.close();
        });

        addPersonButton.setOnAction(action -> //TODO: Add functionality to add a DoB for a Person
        {
            //local Button variables
            BufferedReader br;//BufferedReader to read in the contacts.txt file
            ArrayList<String> fileContents = new ArrayList<>();//ArrayList to store the contents of contacts.txt where each object stored in the ArrayList is its own contact
            PrintWriter writer;//PrintWriter to write out to the contacts.txt file
            String str;//a placeholder string to read in the files and save them to the ArrayList 'fileContents'

            //set the information inputted by the user to the newPerson object
            newPerson.setFirstName(firstNameTextField.getText());
            newPerson.setMiddleName(middleNameTextField.getText());
            newPerson.setLastName(lastNameTextField.getText());
            //newPerson.setDateOfBirth(dateOfBirthTextField.getText());
            newPerson.setPrimaryAddress(primaryAddressTextField.getText());
            newPerson.setSecondaryAddress(secondaryAddressTextField.getText());
            newPerson.setHomePhoneNumber(homePhoneNumberTextField.getText());
            newPerson.setCellPhoneNumber(cellPhoneNumberTextField.getText());
            newPerson.setPersonalEmail(personalEmailTextField.getText());
            newPerson.setWorkEmail(workEmailTextField.getText());

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

                //write out all information on the new person on the next line in contacts.txt
                writer.println(newPerson.getLastName() + ">~>" +
                            newPerson.getFirstName() + ">~>" +
                            newPerson.getMiddleName() + ">~>" +
                            /*newPerson.getDateOfBirth() +*/ ">~>" +
                            newPerson.getPrimaryAddress() + ">~>" +
                            newPerson.getSecondaryAddress() + ">~>" +
                            newPerson.getHomePhoneNumber() + ">~>" +
                            newPerson.getCellPhoneNumber() + ">~>" +
                            newPerson.getPersonalEmail() + ">~>" +
                            newPerson.getWorkEmail() + ">~>");
                writer.close();//don't need this either
            }catch(Exception e)
            {
                System.out.println("Error: " + e.toString());
            }

            tempStage.close();
        });

        tempPane.getChildren().addAll(firstNamePrompt, middleNamePrompt, lastNamePrompt, dateOfBirthPrompt, primaryAddressPrompt, secondaryAddressPrompt, homePhoneNumberPrompt, cellPhoneNumberPrompt, personalEmailPrompt, workEmailPrompt, firstNameTextField, middleNameTextField, lastNameTextField, dateOfBirthTextField, primaryAddressTextField, secondaryAddressTextField, homePhoneNumberTextField, cellPhoneNumberTextField, personalEmailTextField, workEmailTextField, cancelButton, addPersonButton);
        Scene tempScene = new Scene(tempPane);
        tempStage.setScene(tempScene);
        tempStage.showAndWait();
        return newPerson;
    }
}