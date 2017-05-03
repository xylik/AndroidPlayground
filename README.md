# AndroidPlayground
For examples to work create new project through Firebase console and enable analytics(download google-services.json and put it in **app/google-services.json**) for android, after that step create **assets/security/project.properties** file with following properties:  

GOOGLE_PROJECT_ID=<google_console_project_Id>  
GOOGLE_PROJECT_NUMBER=<google_console_project_number>  
API_KEY=<google_console_server_api_key>  
DEBUG_SHA1=<debug_key_sha1>  

eg. /app/src/main/assets/security/project.properties  
GOOGLE_PROJECT_ID=androidplayground-111111  
GOOGLE_PROJECT_NUMBER=111111111111  
API_KEY=AIzaSyAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  
DEBUG_SHA1=11:11:11:11:11:11:11:11:11:11:11:11:11:11:11:11:11:11:11:11  

Further on create new facebook project through facebook developer console and replace **facebook_app_id** key in strings.xml with your application id.
For Firebase and Facebook analytics to work add **SHA1** of your debug key to project key signatures.  
For backup api to work replace **com.google.android.backup.api_key** with your backup api key in AndroidManifest.xml
