package upload;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig(maxFileSize = 16177215)
@WebServlet(name = "uploadImage" , urlPatterns = {"/uploadImage"})
public class uploadImage extends HttpServlet {

	private static final long serialVersionUID = 1L;


	public uploadImage() {
        super();
        
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

		
		/*
		 *  see the video first to configure access to google drive 
			and download your credentials, otherwise you will 
			not be able to do any tests 
		 * 
		 **/
		
		AccionApi.uploadGoogleDrive(request);
	}
}
