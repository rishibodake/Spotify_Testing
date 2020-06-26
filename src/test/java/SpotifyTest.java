import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SpotifyTest {


    private static String token = "";
    private String user_ID = "";
    private int totalNoOfPlayList;
    private final String JSON = "application/json";
    private String playListArray[];
    private String trackIdArray[];



    //Before method
    @BeforeMethod
    public void get() {
        token = "Bearer ";//Latest Token
    }


    @Test(priority = 1)
    public void userID_GET_Request() {
        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/me");
        user_ID = response.path("id");//This step will fetch the user id
        response.then().assertThat().statusCode(200); //Validation of status code
        //System.out.println(user_ID);//optional
    }


    @Test(priority = 2)
    public void userName_GET_Request() {
        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/me");
        String userName = response.path("display_name"); //this step will fetch the user name
        response.then().assertThat().statusCode(200); //Validation of Status code
        //System.out.println(userName);//Optional
    }


    @Test(priority = 3)
    public void userProfile_GET_Request() {
        Response response = given()
                .accept(JSON)
                .contentType(JSON)
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/users/" + user_ID + "/");
        response.then().assertThat().statusCode(200); //Validation of Status code
       // response.prettyPrint(); //print current user profile //optional
    }


    @Test(priority = 4)
    public void playListNumber_GET_Request() {   //get number of total playlist present
        Response response = given()
                .accept(JSON)
                .contentType(JSON)
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/users/" + user_ID + "/playlists");
        response.then().assertThat().statusCode(200);
        totalNoOfPlayList = response.path("total"); //get total playlist
        System.out.println("Total PlayList:" + totalNoOfPlayList);
        //response.prettyPrint(); //Optional
    }


    @Test(priority = 5)
    public void userPlayListInfo_GET_Request() { //get details like UserId of each playlist created
        Response response = given()
                .accept(JSON)
                .contentType(JSON)
                .header("Authorization", token)
                .when()
                .get("https://api.spotify.com/v1/users/" + user_ID + "/playlists");
        //response.prettyPrint(); //optional
        playListArray = new String[totalNoOfPlayList];
        for (int index = 0; index < playListArray.length; index++) {
            playListArray[index] = response.path("items[" + index + "].id"); //get playlist id
        }
      /*  for (String id : playList) { //print play list
            System.out.println("PlayList Id" + id);
        }*/             //optional
    }


    @Test(priority = 6)//10
    public void createPlayList_POST_Request() {
        Response response = given()
                .accept(JSON)
                .contentType(JSON)
                .header("Authorization", token)
                .body("{\"name\": \"Party_Mix_New\",\"description\": \"New playlist description\",\"public\": true}")
                .when()
                .post(" https://api.spotify.com/v1/users/" + user_ID + "/playlists");
        String name = response.path("owner.display_name");
        System.out.println("Name Of Playlist: " + name);
        response.then().assertThat().statusCode(201); //Validation Of PlayList
        //response.prettyPrint(); //optional
    }

    //get List of items in playList
   @Test(priority = 7)
    public void playListItems_GET_Request() {
        Response response = given()
                .accept(JSON)
                .contentType(JSON)
                .header("Authorization", token)
                .when().accept(JSON)
                .get("https://api.spotify.com/v1/playlists/" + playListArray[0] + "/tracks");
        int totalTracks = response.path("total");
        trackIdArray = new String[totalTracks];
        for(int index = 0; index < trackIdArray.length; index++) {
            trackIdArray[index] = response.path("items[" + index + "].track.uri");
        }
    }

    @Test(priority = 8)
    public void changeName_PUT_Request() {
        Response response = given()
                .accept(JSON)
                .contentType(JSON)
                .header("Authorization", token)
                .body("{\"name\": \"Party_Mix_New_Changed\",\"description\": \"New playlist description\",\"public\": true}")
                .when()
                .put("https://api.spotify.com/v1/playlists/" + playListArray[0] + "");
        response.then().assertThat().statusCode(200);//Validation OF Status Code
    }

}


