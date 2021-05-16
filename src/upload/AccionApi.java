package upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class AccionApi {

	private static final long serialVersionUID = 1L;
	
	private static final String APPLICATION_NAME = "Demo Api Google Drive Api";
    
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    //will be created automatically
    private static final String TOKENS_DIRECTORY_PATH = "C:/tokensGoogleDrive";
    
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    
    //your credentials file should be in the src folder
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    
    
    public static void uploadGoogleDrive(HttpServletRequest request) {
    	
    	try {
    		
    		//name input file html
    		Part archivo = request.getPart("imagen");
    		
    		//your temporary folder will be created
    		String rutaTemp = "C:/temp/";
    		
    		String mimeType = archivo.getContentType(); 
    		
    		/*the image is saved in a temporary folder, 
    		then it is deleted when it is uploaded to the google drive*/
    		String infoUploadTemp[] = uploadFilePathTemp(archivo,request,rutaTemp);
    		
    		String path = infoUploadTemp[0];
        	String name = infoUploadTemp[1];

        	uploadDrive(mimeType,path,name);
        	
    	}catch(Exception e) { 
    		e.printStackTrace(); 
    	}
    	
    }
    
    public static String[] uploadFilePathTemp(Part archivo,HttpServletRequest request,String rutaTemp) {
    	
    	String headers = archivo.getHeader("content-disposition");
    	String infoFileUploadTemp[] = getItemsHeaders(headers,rutaTemp);
		
    	try {
    		
    		Files.createDirectories(Paths.get(rutaTemp));
    		rutaTemp = infoFileUploadTemp[0]; 
    		archivo.write(rutaTemp);
    		
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    		
    	}
    	
    	return infoFileUploadTemp;
    }
    
    public static String[] getItemsHeaders(String headers,String rutaTemp) {
    	
    	String arrItems[] = new String[2];
    	String items[] = headers.split(";");
    	
    	for (String item : items) {
            if (item.trim().startsWith("filename")) {
            	String fileName = item.substring(item.indexOf("=") + 2, item.length() - 1); 
            	String name = fileName.split("\\.")[0];
            	arrItems[0] = rutaTemp+fileName;
            	arrItems[1] = name;
            	break;
            }
        }

    	return arrItems;
    }
    
    public static void uploadDrive(String mimeType ,String archivo,String name) throws IOException, GeneralSecurityException{
		
		final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
        buggyLogger.setLevel(java.util.logging.Level.SEVERE);
        
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();
        
        File fileMetadata = new File();
        fileMetadata.setName(name);
        java.io.File filePath = new java.io.File(archivo);
        FileContent mediaContent = new FileContent(mimeType,filePath);
        File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
        System.out.println("File ID: " + file.getId());
        filePath.delete();
	}

	
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    	
    	final java.util.logging.Logger buggyLogger = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName());
        buggyLogger.setLevel(java.util.logging.Level.SEVERE);

        InputStream in = uploadImage.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
