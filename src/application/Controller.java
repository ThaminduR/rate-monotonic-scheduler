package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import application.Scheduler;
import application.Task;
import application.TimelineChart;
import javafx.scene.control.TextArea;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Controller {

    private ObservableList<Task> ProcessList = FXCollections.observableArrayList();
    private String[] style = {"status-one", "status-two", "status-three",
            "status-four", "status-five", "status-six", "status-seven", "status-eight", "status-nine", "status-ten",
            "status-eleven", "status-twelve"};
    private List<String> styleList;
    @FXML
    private TextField ExecutionTime;
    @FXML
    private TextField Period;
    @FXML
    private TableView<Task> ProcessTable;
    @FXML
    private TableColumn ProcessIDCol;
    @FXML
    private TableColumn ExecutionCol;
    @FXML
    private TableColumn PeriodCol;
    @FXML
    private TextArea LCM;
    @FXML
    private TextArea Message;

    public Controller(){
    	Platform.runLater(new Runnable(){
    		@Override
            public void run() {
                ProcessTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                    	ExecutionTime.setText(Integer.toString(newSelection.getET()));
                    	Period.setText(Integer.toString(newSelection.getPeriod()));
                    }
                });
            }
        });
    }

    	@FXML
        private void addTask() {

            ProcessList.add(new Task(Integer.parseInt(ExecutionTime.getText()),Integer.parseInt(Period.getText())));

            ExecutionCol.setCellValueFactory(new PropertyValueFactory<>("eT"));
            PeriodCol.setCellValueFactory(new PropertyValueFactory<>("period"));
            ProcessIDCol.setCellValueFactory(new PropertyValueFactory<>("name"));

            ProcessTable.setItems(ProcessList);
            ExecutionTime.setText("");
            Period.setText("");
            LCM.setText(Integer.toString(Scheduler.calcLCM(ProcessList)));
            Message.setText(Scheduler.Cal(ProcessList));
        }

    	@FXML
        private void schedule() {

            styleList = new ArrayList<>(Arrays.asList(style));
            TimelineChart chart = drawChart();
            int width = 800; //Integer.parseInt(lblLCM.getText()) * 55;
            int height = 400; //ProcessList.size() * 20 * 2 + 100;
            Scene scene  = new Scene(chart, width, height);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }

    	private TimelineChart drawChart() {

            List<Task> resultList = new ArrayList<>(Scheduler.schedule(ProcessList));
            List<String> nameList = new ArrayList<>();
            for(Task t : ProcessList)
                nameList.add(t.getName());

            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Execution Time");
            xAxis.setAutoRanging(false);
            xAxis.setMinorTickCount(5);
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(Double.parseDouble(LCM.getText()));
            xAxis.setTickUnit(1);
            CategoryAxis yAxis = new CategoryAxis();
            yAxis.setLabel("Process ID");
            yAxis.setTickLabelGap(10);
            yAxis.setAutoRanging(false);
            yAxis.setCategories(FXCollections.observableArrayList(nameList));

            final TimelineChart chart = new TimelineChart(xAxis, yAxis);
            chart.setTitle("Rate Monotonic Schedule Gantt Chart");
            chart.setLegendVisible(false);

            ObservableList<XYChart.Series<Number, String>> chartData = FXCollections.observableArrayList();
            for(Task t : ProcessList) {
                ObservableList<XYChart.Data<Number, String>> seriesData = FXCollections.observableArrayList();
                String styleClass = getRandomStyle();
                for(int i = 0; i < resultList.size(); i++) {
                    if(t.equals(resultList.get(i)))
                        seriesData.add(new XYChart.Data<>(i, t.getName(), new TimelineChart.ExtraData(styleClass)));
                }
                chartData.add(new XYChart.Series<>(seriesData));
            }
            chart.setData(chartData);
            chart.getStylesheets().add(getClass().getResource("timeline.css").toExternalForm());

            return chart;
        }

    	private String getRandomStyle() {
            Random random = new Random();
            int randomIndex = random.nextInt(styleList.size());
            String randomStyle = styleList.get(randomIndex);
            styleList.remove(randomIndex);
            return randomStyle;
        }


}