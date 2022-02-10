import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class WindowBuilder extends Application {

        public static final String BLANK = "";

        @Override
        public void start(Stage stage) {
                stage.setTitle("Employees-JavaFX");

                				GridPane leftGrid = new Grid Pane();
                	leftGrid.setAlignment(Pos.CENTER);
                	leftGrid.setHgap(10);
                	leftGrid.setVgap(10);
                	leftGrid.setPadding(new Insets(25, 25, 25, 25));
                
                	TextField idField = new TextField();
                	idField.setVisible(false);
                	leftGrid.add(idField, 1, 1);

                	Label authorLabel = new Label("Author:");
                	leftGrid.add(authorLabel, 0, 1);

                	TextField authorField = new TextField();
                	leftGrid.add(authorField, 1, 1);

                	Label titleLabel = new Label("Title:");
                	leftGrid.add(titleLabel, 0, 2);

                	TextField titleField = new TextField();
                	leftGrid.add(titleField, 1, 2);

	                Label yearLabel = new Label("Year:");
	                leftGrid.add(yearLabel, 0, 3);

	                TextField yearField = new TextField();
	                leftGrid.add(yearField, 1, 3);

	                Label priceLabel = new Label("Price:");
	                leftGrid.add(priceLabel, 0, 4);

	                TextField priceField = new TextField();
	                leftGrid.add(priceField, 1, 4);
                
	                Button findButton = new Button("Find");
	                Button updateButton = new Button("Update");
                
                HBox hBoxDisplay = new HBox(10, findButton, updateButton);
                leftGrid.add(hBoxDisplay, 1, 5);

                findButton.setOnAction(actionEvent -> {
                        String title = titleField.getText();
                        if (!"".equals(title)) {
                                Connection connection = null;
                                PreparedStatement statement = null;
                                try {
                                        connection = SqliteConnection.dbConnector();
                                        connection.setAutoCommit(false);
                                        String query = "SELECT ID, AUTHOR, TITLE, YEAR, PRICE FROM BOOK WHERE TITLE = ?";
                                        statement = connection.prepareStatement(query);
                                        statement.setString(1, title);
                                        ResultSet rs = statement.executeQuery();
                                        
                                        if (rs.next()) {
                                                idField.setText(String.valueOf(rs.getInt(1)));
                                                authorField.setText(rs.getString(2));
                                                titleField.setText(rs.getString(3));
                                                //titleField.setEditable(false);
                                                yearField.setText(String.valueOf(rs.getInt(4)));
                                                priceField.setText(String.valueOf(rs.getDouble(5)));
                                        } else {
                                                authorField.setText("");                                                
                                                yearField.setText("");
                                                priceField.setText("");
                                                this.alert("Error", "No Book available with title "+title, AlertType.ERROR);
                                                titleField.setText("");
                                        }
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }                       
                        }
                });
                
                
                updateButton.setOnAction(actionEvent -> {
                        String id = idField.getText();
                        String title = titleField.getText();
                        String author = authorField.getText();
                        String year = yearField.getText();
                        String price = priceField.getText();
                        if (!BLANK.equals(title) && !BLANK.equals(author) && !BLANK.equals(year) && !BLANK.equals(price)) {
                                Connection connection = null;
                                PreparedStatement statement = null;
                                try {
                                        connection = SqliteConnection.dbConnector();
                                        connection.setAutoCommit(false);
                                        String query = "UPDATE BOOK SET AUTHOR = ?, TITLE = ? YEAR = ?, PRICE = ? WHERE ID = ?";
                                        statement = connection.prepareStatement(query);
                                        statement.setString(1, author);
                                        statement.setString(2, title);
                                        statement.setInt(2, Integer.parseInt(year));
                                        statement.setDouble(3, Double.parseDouble(price));
                                        statement.setInt(4, Integer.parseInt(id));
                                        int count = statement.executeUpdate();
                                        
                                        if (count == 1) {
                                                this.alert("Update", "Successful!", AlertType.INFORMATION);
                                        } else {
                                                this.alert("Error", "Failed!", AlertType.ERROR);
                                        }
                                        
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }                       
                        }
                });
                
                
                	GridPane rightGrid = new GridPane();
                	rightGrid.setVgap(10);
                	Button displayRecordsButton = new Button("Display Records");
                	rightGrid.add(displayRecordsButton, 0, 1);
                
                		TextArea textArea = new TextArea();
                			textArea.setPrefSize(200, 250);
                			rightGrid.add(textArea, 0, 3);
                
                			displayRecordsButton.setOnAction(actionEvent -> {
                				StringBuffer sb = new StringBuffer("");
                				Connection connection = null;
                				PreparedStatement statement = null;
                					try {
                                connection = SqliteConnection.dbConnector();
                                connection.setAutoCommit(false);
                                String query = "SELECT AUTHOR, TITLE, YEAR, PRICE FROM BOOK";
                                statement = connection.prepareStatement(query);
                                ResultSet rs = statement.executeQuery();
                                
                                while (rs.next()) {
                                        sb.append(rs.getString(1)+", "+rs.getString(2)+", "+rs.getInt(3)+", "+rs.getDouble(4)+"\n");                            
                                }
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                        textArea.setText(sb.toString());
                
                });
                
                HBox hBox = new HBox(leftGrid, rightGrid);
                
                Scene scene = new Scene(hBox, 500, 350);
                stage.setScene(scene);

                stage.show();
        	}

        		public void alert(String title, String message, AlertType alertType) {
        			Alert alert = new Alert(alertType);
        			alert.setTitle(title);
        			alert.setHeaderText(null);
        			alert.setContentText(message);
        			alert.showAndWait();
        }

        public static void main(String[] args) {
                launch(args);
        }
}