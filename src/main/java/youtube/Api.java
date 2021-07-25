package youtube;
//get videos from playlist to be added to your own playlist using oauth2 and youtube data API v3

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.ResourceId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.security.CodeSource;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Api {
    //global variables
    private static final String CLIENT_SECRETS= "/client_secret.json";
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final String APPLICATION_NAME = "API code samples";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static String getVideosPlayListID = "";     //ID of playlist to get videos from (my songs playlist)
    private static String addToPlayListID = "";      //ID of playlist to add videos to (my testing playlist)
    private static ArrayList<String> vids = new ArrayList<>();      //array list for storing videos to be added
    private static YouTube youtubeService;

    //get videos from, set playlistID
    public void setGetVideosPlayListID(String URL){
        getVideosPlayListID = URL.substring(38);
    }

    //add videos to, set playlistID
    public void setAddToPlayListID(String URL){
        addToPlayListID = URL.substring(38);
    }

    //build api service
    public YouTube getService() throws GeneralSecurityException, IOException, URISyntaxException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    //get credentials from oauth2 file
    public Credential authorize(final NetHttpTransport httpTransport) throws IOException, URISyntaxException {
        // Load client secrets.
        //InputStream in = Api.class.getResourceAsStream(CLIENT_SECRETS);
        //get path of jar file to read secrets file in same directory
        CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        String jarDir = jarFile.getParentFile().getPath();
        System.out.println("jarDir path is " + jarDir);
        String secrets = jarDir + CLIENT_SECRETS;
        System.out.println("secrets path is " + secrets);
        FileInputStream is = new FileInputStream(secrets);
        InputStreamReader isr = new InputStreamReader( is, "UTF-8" );





        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new BufferedReader(isr));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();
        Credential credential =
                new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    //get credentials when program starts
    public void startup() throws GeneralSecurityException, IOException, URISyntaxException {
        vids.clear();         //clear videos to be added
        youtubeService = getService();
    }


    //call everything
    public void execute() throws GeneralSecurityException, IOException, GoogleJsonResponseException, Exception {
        // Define and execute the API request
        YouTube.PlaylistItems.List request = youtubeService.playlistItems()
                .list("contentDetails");
        PlaylistItemListResponse response = request.setMaxResults(50L).setPlaylistId(getVideosPlayListID)
                .execute();

        boolean firstIteration = true;
        String nextPage ="";
        JSONObject o = new JSONObject(response);
        //while there is a page token or first iteration continue
        while (o.has("nextPageToken") || firstIteration == true){                 //if first time get items and check if more pages needed
            if (firstIteration == true){
                firstIteration = false;
                JSONArray items = o.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    String ID = items.getJSONObject(i).getJSONObject("contentDetails").getString("videoId");
                    vids.add(ID);
                }
                //if there is not another page break
                if (o.has("nextPageToken")){
                    nextPage = response.getNextPageToken();
                }
                else {
                    break;
                }

            } else {
                //if more pages needed reitereate with page token from previous page
                response = request.setPageToken(nextPage).setMaxResults(50L).setPlaylistId(getVideosPlayListID)
                        .execute();
                o = new JSONObject(response);
                JSONArray items = o.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    String ID = items.getJSONObject(i).getJSONObject("contentDetails").getString("videoId");
                    vids.add(ID);
                }
                nextPage = response.getNextPageToken();
            }
        }
//        System.out.println("Here are the video ID's:");
//        for (int i=0; i<vids.size(); i++){
//            System.out.println(i + " " + vids.get(i));
//        }


        //add videos to playlist
        PlaylistItem playlistItem = new PlaylistItem();
        PlaylistItemSnippet snippet = new PlaylistItemSnippet();
        snippet.setPlaylistId(addToPlayListID);
        ResourceId resourceId = new ResourceId();
        resourceId.setKind("youtube#video");

        for (String id: vids){
            try{
                resourceId.setVideoId(id);
                snippet.setResourceId(resourceId);
                playlistItem.setSnippet(snippet);
                YouTube.PlaylistItems.Insert vidRequest = youtubeService.playlistItems()
                        .insert("snippet", playlistItem);
                PlaylistItem vidResponse = vidRequest.execute();
            } catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }

    }
}