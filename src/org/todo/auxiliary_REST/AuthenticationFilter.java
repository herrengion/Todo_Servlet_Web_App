package org.todo.auxiliary_REST;

import javax.security.auth.login.LoginException;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import org.todo.*;
import users.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.todo.TodoServlet.DATA_PATH_WEB_INF_DATA;

@WebFilter(urlPatterns = {"/todos", "/categories"})
public class AuthenticationFilter extends HttpFilter {

	UserList userDB = new UserList();
	private String servletContextPath;
	private ServletContext servletContext;

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request.getMethod().equals("POST")) {
			try {
				String authHeader = request.getHeader("Authorization");
				String scheme = authHeader.split(" ")[0];
				if (!scheme.equals("Basic")) throw new Exception();
				String credentials = authHeader.split(" ")[1];
				credentials = new String(DatatypeConverter.parseBase64Binary(credentials), ISO_8859_1);
				String username = credentials.split(":")[0];
				String password = credentials.split(":")[1];
				servletContext = request.getServletContext();
				servletContextPath = request.getContextPath();
				File userList = new File(servletContextPath+DATA_PATH_WEB_INF_DATA+"/UserList.xml");
				File userListSchema = new File(servletContextPath+DATA_PATH_WEB_INF_DATA+"/UserList.xsd");
				if(!(userList.isFile() || !userListSchema.isFile()))
				{
					throw new LoginException("User data are not available or the corresponding schema to check the data!");
				}
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(UserList.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					Schema schema = schemaFactory.newSchema(userListSchema);
					unmarshaller.setSchema(schema);
					userDB = (UserList) unmarshaller.unmarshal(userList);
				}
				catch (org.xml.sax.SAXException e){
					throw new LoginException("Login failed due to Servlet problems at Login stage" +
							": "+e.getMessage());
				}
				catch (JAXBException e){
					throw new LoginException("Login failed due to JAXB Problems at Login stage" +
							": "+e.getMessage());
				}

				for(int i = 0; i<userDB.getUser().size(); i++){
					if(userDB.getUser().get(i).getUsername().equals(username)){
						if(!(userDB.getUser().get(i).getPassword().equals(password))){
							throw new Exception();
						}
					}
				}
			} catch (Exception ex) {
				response.setStatus(SC_UNAUTHORIZED);
				return;
			}
		}
		chain.doFilter(request, response);
	}
}
