package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main extends Application {
    static int threadCount;
    static String keyword1, keyword2, keyword3, subreddit, type, sortedBy;



    static public ProgressBar progressBar = new ProgressBar(0);
    static Label statusUpdatedLabel = new Label(Integer.toString((int) Math.round(progressBar.getProgress()*100)) + "%");


    static Spinner<Integer> theadCountTF = new Spinner ();
    static TextField keyword1TF = new TextField ();
    static public TextField subredditTF = new TextField ();
    static public TextField keyword2TF = new TextField ();
    static public TextField keyword3TF = new TextField ();
    static public Button searchBtn = new Button("Search");
    static public ListView<Hyperlink> allList = new ListView<>();
    static ListView<Hyperlink> filteredList = new ListView<>();
    static ArrayList<Hyperlink> allHyperlinkList = new ArrayList<>();
    static ArrayList<Hyperlink> filteredHyperlinkList = new ArrayList<>();
    public static boolean isFiltered = true;

    static final ToggleGroup typeTG = new ToggleGroup();
    static final ToggleGroup pageTG = new ToggleGroup();
    static final ToggleGroup sortTG = new ToggleGroup();

    static RadioButton urlRB = new RadioButton("URL");
    static RadioButton keywordRB = new RadioButton("Keyword");
    static RadioButton frontPageRB = new RadioButton("Front Page");
    static RadioButton allRB = new RadioButton("All");
    static RadioButton subredditRB = new RadioButton("Subreddit:");
    static RadioButton hotRB = new RadioButton("Hot");
    static RadioButton allTimeRB = new RadioButton("All Time");
    static RadioButton yearRB = new RadioButton("Year");
    static RadioButton monthRB = new RadioButton("Month");
    static RadioButton weekRB = new RadioButton("Week");
    static RadioButton dayRB = new RadioButton("Day");

    static CheckBox imgurFilterCB = new CheckBox();
    static CheckBox keyword1CB = new CheckBox();
    static CheckBox keyword2CB = new CheckBox();
    static CheckBox keyword3CB = new CheckBox();

    static Spinner<Integer> minImageSpinner = new Spinner();

    public void createProgressUI(Pane pane){
        progressBar.setPrefWidth(110);
        progressBar.setLayoutX(595);
        progressBar.setLayoutY(634);
        pane.getChildren().addAll(progressBar);
    }

    public void createLabels(Pane pane){
        Label redditSearchBotLabel = new Label("Reddit Search Bot");
        Label allResultsLabel = new Label("All Results");
        Label threadCountLabel = new Label("Thread Count:");
        Label imgurFilterLabel = new Label("Imgur Filter");
        Label imageMinLabel = new Label("Image Min.: ");
        Label statusLabel = new Label("Status: ");


        redditSearchBotLabel.setFont(new Font("Arial", 40));
        allResultsLabel.setFont(new Font("Arial", 18));
        threadCountLabel.setFont(new Font("Arial", 18));
        imgurFilterLabel.setFont(new Font("Arial", 18));
        imageMinLabel.setFont(new Font("Arial", 18));
        statusLabel.setFont(new Font("Arial", 15));
        statusUpdatedLabel.setFont(new Font("Arial", 15));

        redditSearchBotLabel.setLayoutX(496);
        redditSearchBotLabel.setLayoutY(53);

        allResultsLabel.setLayoutX(16);
        allResultsLabel.setLayoutY(98);

        threadCountLabel.setLayoutX(487);
        threadCountLabel.setLayoutY(134);

        imgurFilterLabel.setLayoutX(1000);
        imgurFilterLabel.setLayoutY(98);

        imageMinLabel.setLayoutX(1126);
        imageMinLabel.setLayoutY(98);

        statusLabel.setLayoutX(496);
        statusLabel.setLayoutY(634);

        statusUpdatedLabel.setLayoutX(550);
        statusUpdatedLabel.setLayoutY(634);

        pane.getChildren().add(redditSearchBotLabel);
        pane.getChildren().add(allResultsLabel);
        pane.getChildren().add(threadCountLabel);
        pane.getChildren().add(imgurFilterLabel);
        pane.getChildren().add(imageMinLabel);
        pane.getChildren().add(statusLabel);
        pane.getChildren().add(statusUpdatedLabel);

    }

    public void createLists(Pane pane){
        allList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        filteredList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        allList.setPrefWidth(464);
        allList.setPrefHeight(476);

        filteredList.setPrefWidth(464);
        filteredList.setPrefHeight(476);

        allList.setLayoutX(16);
        allList.setLayoutY(134);

        filteredList.setLayoutX(822);
        filteredList.setLayoutY(134);

        pane.getChildren().add(allList);
        pane.getChildren().add(filteredList);
    }

    public void createTextFields(Pane pane){
        subredditTF.setDisable(true);
        keyword2TF.setDisable(true);
        keyword3TF.setDisable(true);

        theadCountTF.setPrefWidth(124);
        theadCountTF.setPrefHeight(27);

        subredditTF.setPrefWidth(124);
        subredditTF.setPrefHeight(27);

        keyword1TF.setPrefWidth(105);
        keyword1TF.setPrefHeight(27);

        keyword2TF.setPrefWidth(105);
        keyword2TF.setPrefHeight(27);

        keyword3TF.setPrefWidth(105);
        keyword3TF.setPrefHeight(27);

        theadCountTF.setLayoutX(487);
        theadCountTF.setLayoutY(160);

        subredditTF.setLayoutX(487);
        subredditTF.setLayoutY(285);

        keyword1TF.setLayoutX(707);
        keyword1TF.setLayoutY(203);

        keyword2TF.setLayoutX(707);
        keyword2TF.setLayoutY(243);

        keyword3TF.setLayoutX(707);
        keyword3TF.setLayoutY(285);

        pane.getChildren().addAll(subredditTF, keyword1TF, keyword2TF, keyword3TF);
    }

    public void createRadioButtonGroups(Pane pane){

        urlRB.fontProperty().set(new Font("Arial", 18));
        keywordRB.fontProperty().set(new Font("Arial", 18));

        frontPageRB.fontProperty().set(new Font("Arial", 18));
        allRB.fontProperty().set(new Font("Arial", 18));
        subredditRB.fontProperty().set(new Font("Arial", 18));

        hotRB.fontProperty().set(new Font("Arial", 18));
        allTimeRB.fontProperty().set(new Font("Arial", 18));
        yearRB.fontProperty().set(new Font("Arial", 18));
        monthRB.fontProperty().set(new Font("Arial", 18));
        weekRB.fontProperty().set(new Font("Arial", 18));
        dayRB.fontProperty().set(new Font("Arial", 18));

        urlRB.setToggleGroup(typeTG);
        keywordRB.setToggleGroup(typeTG);

        frontPageRB.setToggleGroup(pageTG);
        allRB.setToggleGroup(pageTG);
        subredditRB.setToggleGroup(pageTG);

        hotRB.setToggleGroup(sortTG);
        allTimeRB.setToggleGroup(sortTG);
        yearRB.setToggleGroup(sortTG);
        monthRB.setToggleGroup(sortTG);
        weekRB.setToggleGroup(sortTG);
        dayRB.setToggleGroup(sortTG);

        urlRB.setLayoutX(707);
        urlRB.setLayoutY(134);
        keywordRB.setLayoutX(707);
        keywordRB.setLayoutY(160);

        frontPageRB.setLayoutX(487);
        frontPageRB.setLayoutY(201);
        allRB.setLayoutX(487);
        allRB.setLayoutY(227);
        subredditRB.setLayoutX(487);
        subredditRB.setLayoutY(253);

        hotRB.setLayoutX(614);
        hotRB.setLayoutY(385);
        allTimeRB.setLayoutX(614);
        allTimeRB.setLayoutY(411);
        yearRB.setLayoutX(614);
        yearRB.setLayoutY(437);
        monthRB.setLayoutX(614);
        monthRB.setLayoutY(463);
        weekRB.setLayoutX(614);
        weekRB.setLayoutY(489);
        dayRB.setLayoutX(614);
        dayRB.setLayoutY(515);

        frontPageRB.setSelected(true);
        urlRB.setSelected(true);
        hotRB.setSelected(true);


        subredditRB.selectedProperty().addListener(event -> subredditTF.setDisable(subredditRB.isSelected() ? false : true));

        pane.getStylesheets().add(getClass().getResource("/sample/radio.css").toExternalForm());
        pane.getChildren().addAll(urlRB, keywordRB, frontPageRB, allRB, subredditRB, hotRB, allTimeRB, yearRB, monthRB, weekRB, dayRB);
    }

    public void createCheckBoxes(Pane pane){
        imgurFilterCB.setSelected(true);
        keyword1CB.setSelected(true);
        keyword1CB.setDisable(true);

        imgurFilterCB.setLayoutX(975);
        imgurFilterCB.setLayoutY(100);

        keyword1CB.setLayoutX(680);
        keyword1CB.setLayoutY(205);

        keyword2CB.setLayoutX(680);
        keyword2CB.setLayoutY(246);

        keyword3CB.setLayoutX(680);
        keyword3CB.setLayoutY(289);

        keyword2CB.selectedProperty().addListener(event -> keyword2TF.setDisable(keyword2CB.isSelected() ? false : true));
        keyword3CB.selectedProperty().addListener(event -> keyword3TF.setDisable(keyword3CB.isSelected() ? false : true));

        imgurFilterCB.selectedProperty().addListener(event -> isFiltered = imgurFilterCB.isSelected() ? true : false);

        pane.getChildren().addAll(imgurFilterCB, keyword1CB, keyword2CB, keyword3CB);
    }

    public void createSpinners(Pane pane){
        minImageSpinner.setEditable(true);
        theadCountTF.setEditable(true);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 21, 11);
        minImageSpinner.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> threadFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500, 500);
        theadCountTF.setValueFactory(threadFactory);

        minImageSpinner.setPrefWidth(55);
        minImageSpinner.setPrefHeight(30);

        minImageSpinner.setLayoutX(1230);
        minImageSpinner.setLayoutY(95);

        pane.getChildren().add(minImageSpinner);
        pane.getChildren().add(theadCountTF);
    }

    public void createButtons(Pane pane){
        Button allResultsAllBtn = new Button("Open All");
        Button allResultsSelectedBtn = new Button("Open Selected");
        Button filteredAllBtn = new Button("Open All");
        Button filteredSelectedBtn = new Button("Open Selected");

        searchBtn.setFont(new Font("Arial", 18));
        allResultsAllBtn.setFont(new Font("Arial", 18));
        allResultsSelectedBtn.setFont(new Font("Arial", 18));
        filteredAllBtn.setFont(new Font("Arial", 18));
        filteredSelectedBtn.setFont(new Font("Arial", 18));

        searchBtn.setPrefWidth(110);
        searchBtn.setPrefHeight(30);

        searchBtn.setLayoutX(595);
        searchBtn.setLayoutY(560);

        allResultsAllBtn.setPrefWidth(232);
        allResultsAllBtn.setPrefHeight(30);

        allResultsSelectedBtn.setPrefWidth(232);
        allResultsSelectedBtn.setPrefHeight(30);

        allResultsAllBtn.setLayoutX(16);
        allResultsAllBtn.setLayoutY(625);

        allResultsSelectedBtn.setLayoutX(248);
        allResultsSelectedBtn.setLayoutY(625);

        filteredAllBtn.setLayoutX(822);
        filteredAllBtn.setLayoutY(625);

        filteredAllBtn.setPrefWidth(232);
        filteredAllBtn.setPrefHeight(30);

        filteredSelectedBtn.setLayoutX(1054);
        filteredSelectedBtn.setLayoutY(625);

        filteredSelectedBtn.setPrefWidth(232);
        filteredSelectedBtn.setPrefHeight(30);

        searchBtn.setOnAction(event -> onSearch());
        allResultsAllBtn.setOnAction(event -> onOpenAll(allHyperlinkList));
        allResultsSelectedBtn.setOnAction(event -> onOpenSelected(allList));
        filteredAllBtn.setOnAction(event -> onOpenAll(filteredHyperlinkList));
        filteredSelectedBtn.setOnAction(event -> onOpenSelected(filteredList));

        pane.getChildren().addAll(searchBtn, allResultsAllBtn, allResultsSelectedBtn, filteredAllBtn, filteredSelectedBtn);
    }

    public void onSearch(){
        if(getInput()){
            searchBtn.setDisable(true);
            searchBtn.setText("Searching");
            progressBar.setProgress(0);
            statusUpdatedLabel.setText(Integer.toString((int) Math.round(progressBar.getProgress()*100)) + "%");
            allList.getItems().clear();
            filteredList.getItems().clear();
            allHyperlinkList.clear();
            filteredHyperlinkList.clear();
            Service process = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            runBatch();
                            return null;
                        }
                    };
                }
            };
            process.start();
            process.setOnSucceeded(succeed -> updateGUI());
        }
    }

    public void updateGUI(){
        updateListView(allList, allHyperlinkList, "X:\\User\\Documents\\reddit-search-bot\\new.txt");
        if(isFiltered){
            updateListView(filteredList, filteredHyperlinkList, "X:\\User\\Documents\\reddit-search-bot\\filtered.txt");
        }
        searchBtn.setDisable(false);
        searchBtn.setText("Search");
        progressBar.setProgress(1);
        statusUpdatedLabel.setText(Integer.toString((int) Math.round(progressBar.getProgress()*100)) + "%");
    }

    public void updateListView(ListView lv, ArrayList<Hyperlink> hyperlinkList, String path){
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(e -> hyperlinkList.add(new Hyperlink(e)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        IntStream.range(0, hyperlinkList.size()).forEachOrdered(link -> lv.getItems().add(hyperlinkList.get(link)));
        hyperlinkList.forEach(hyperlink -> hyperlink.setOnAction(t -> {
            getHostServices().showDocument(hyperlink.getText());
        }));
    }

    public void onOpenAll(ArrayList<Hyperlink> list){
        IntStream.range(0, list.size()).forEach((link) -> getHostServices().showDocument(list.get(link).getText()));
    }

    public void onOpenSelected(ListView lv){
        ObservableList<Hyperlink> selectedItems =  lv.getSelectionModel().getSelectedItems();
        selectedItems.forEach(selectedItem -> getHostServices().showDocument(selectedItem.getText()));
    }

    public static void updateProgress(String prog){
        Platform.runLater(() -> {
            try{
                progressBar.setProgress(Double.parseDouble(prog.length() < 5 ? prog : prog.substring(0, 5))/100);
                statusUpdatedLabel.setText(Integer.toString((int) Math.round(progressBar.getProgress()*100)) + "%");
            }catch (Exception a){
                try{
                    progressBar.setProgress(Double.parseDouble(prog.length() < 4 ? prog : prog.substring(0, 4))/100);
                    statusUpdatedLabel.setText(Integer.toString((int) Math.round(progressBar.getProgress()*100)) + "%");
                }catch (Exception b){
                    try{
                        progressBar.setProgress(Double.parseDouble(prog.length() < 3 ? prog : prog.substring(0, 3))/100);
                        statusUpdatedLabel.setText(Integer.toString((int) Math.round(progressBar.getProgress()*100)) + "%");
                    }catch (Exception c){
                        progressBar.setProgress(-1);
                    }
                }
            }
        });
    }

    public static void runBatch(){
        String argStr = "\"" + threadCount + "," + subreddit + ","  + type + ","  + sortedBy + "," + keyword1 + ","  + keyword2 + ","  + keyword3 + "\"";
        try{
            File batFile = new File("X:\\User\\Documents\\reddit-search-bot\\bot-batch.bat");
            try {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", batFile.getName() + " " + argStr + " " + minImageSpinner.getValue());
                pb.directory(new File(batFile.getParent().replace("\\","/") + "/"));
                pb.redirectErrorStream(true);
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    updateProgress(line);
                    System.out.println(line);
                }
                //p.waitFor();
            } catch (IOException ex) {
                System.out.println("Error: Unable to find batch file location");
            }
        }catch(Exception e){
            System.out.println("Error: Unable to find batch file location");
        }
        System.out.println("End of run method.");
    }

    public boolean getInput(){
        boolean isValid;
        keyword1 = keyword2 = keyword3 = subreddit = type = sortedBy = null;
        try {
            threadCount = theadCountTF.getValue();
            theadCountTF.setStyle("");
            isValid = true;
        }catch (NumberFormatException e) {
            theadCountTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            isValid = false;
        }
        if(isValid){
            isValid = stringTFValidate(keyword1TF, "keyword1");
        }
        if(isValid){
            if(!subredditTF.isDisable()){
                isValid = stringTFValidate(subredditTF, "subreddit");
            }else if(frontPageRB.isSelected()){
                subreddit = "Front Page";
            }else if(allRB.isSelected()){
                subreddit = "All";
            }
            if(!keyword2TF.isDisable()){
                isValid = stringTFValidate(keyword2TF, "keyword2");
            }
            if(!keyword3TF.isDisable()){
                isValid = stringTFValidate(keyword3TF, "keyword3");
            }
        }
        if(isValid){
            RadioButton typeRB = (RadioButton) typeTG.getSelectedToggle();
            type = typeRB.getText();
            RadioButton sortRB = (RadioButton) sortTG.getSelectedToggle();
            sortedBy = sortRB.getText();
        }
        return isValid;
    }

    public boolean stringTFValidate(TextField tf, String str){
        boolean isValid;
        if(tf.getText() != null && !tf.getText().isEmpty()){
            if(str.equals("subreddit")){
                subreddit = tf.getText();
            }
            if(str.equals("keyword1")){
                keyword1 = tf.getText();
            }
            if(str.equals("keyword2")){
                keyword2 = tf.getText();
            }
            if(str.equals("keyword3")){
                keyword3 = tf.getText();
            }
            tf.setStyle("");
            isValid = true;
        }else{
            tf.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane pane = new Pane();
        primaryStage.setTitle("Reddit Search Bot");
        createProgressUI(pane);
        createLabels(pane);
        createLists(pane);
        createTextFields(pane);
        createRadioButtonGroups(pane);
        createCheckBoxes(pane);
        createSpinners(pane);
        createButtons(pane);
        primaryStage.setScene(new Scene(pane, 1300, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
