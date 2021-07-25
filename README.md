# Youtube-Playlist-Adder
The purpose of this was Youtube does not allow you to add all videos from someone else's playlist to your own. You have to add each video manually.
This simple GUI allows the desired URLs to be specified and added using Oauth2.0 verification.<br/><br/>
**Zip file is available for download with jar**

## How to Setup:
<ol>
<li>Have at least java 8 downloaded (jre8/1.8)</li>
<li> Unzip the contents of the file</li>
<li>The client_secrets must be in the same directory as jar file to work</li>
<li>Go to Google Developer Console @ https://console.cloud.google.com/apis/dashboard</li>
<li>Select create new project</li>
<li>Complete the prompts</li>
<li>Then select the project from the dashboard</li>
<li>Select enable APIS and Services</li>
<li>Search for Youtube Data API v3 and enable</li>
<li>Go back to dashboard and select oauth consent screen</li>
<li>Complete prompts</li>
<li>Select Credentials</li>
<li>Create Oauth2.0 Credentials</li>
<li>Select edit and click download client secrets json file</li>
<li>Copy what is in json file and paste into client_secret in jar directory</li>
</ol>

## How to Use:
<ol>
<li>If client secrets file contents are correctly added and is in same directory as jar file it is ready for use.</li>
<li>Double click the jar (open with java/jre1.8/bin/javaw.exe)</li>
<li>You will be prompted to login with your google account in a browser, select yes.</li>
<li>Paste the URL of the playlist you would like to get videos from</li>
<li>Next paste the URL of the playlist you want the videos added to</li>
<li>Click Run and wait for videos to get added</li>
<li>You can check you quota left per day @ https://console.cloud.google.com/iam-admin/quotas/details
    You only get 10,000 quota per day for free and 1 video read request cost 1 quota while 1 video add 
    request costs 50 quota. You can add about 200 videos before your quota maxes out. You can also create a 
    new project with credentials and replace client_secret.json with different oauth2.0 credentials.</li>
</ol>
