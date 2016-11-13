package xml_things;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONArray;

import daos.AuctionDAOImpl;
import daos.CategoryDAOImpl;
import daos.UserDAOImpl;
import model.Auction;
import model.Category;
import model.User;

/**
 * Servlet implementation class upload
 */
//@WebServlet("/upload")
@WebServlet(name="upload",urlPatterns={"/upload"})
@MultipartConfig(maxFileSize = 16177215)  
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	/*public upload() {
		super();
		// TODO Auto-generated constructor stub
	}*/

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/*@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());

	}
	 */
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		response.setContentType("text/html;charset=UTF-8");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(!isMultipart){
			System.out.println("its not multy dammit!!!");
			return;
		}

		/*if(isMultipart){
	        FileItemFactory factory = new DiskFileItemFactory();
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        try{
	            List<FileItem> fields = upload.parseRequest((RequestContext) request);
	            Iterator<FileItem> it = fields.iterator();
	            while (it.hasNext()) {
	                FileItem fileItem = it.next();

	                out.println(fileItem.getString());
	          }
	        }catch (FileUploadException e) {
	            e.printStackTrace();
	        }       
	    }*/
		int hola=0;
		for (Part part : request.getParts()) {
			hola++;
			File file = new File("C:\\Users\\Stavros\\Desktop\\yolo.xml");
			OutputStream os = new FileOutputStream(file);
			InputStream is = null;
			is = part.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
				//  String str = new String(bytes, StandardCharsets.UTF_8);
				// System.out.println(str + "DDDDDDD\n");
				//System.out.println(read);
			}
			is.close();
			os.close();    
			/* is = new FileInputStream(file);
	            while((read = is.read(bytes)) != -1) {

	            	String str = new String(bytes, StandardCharsets.UTF_8);
	                System.out.println(str + "DDDDDDD\n");
	            }
	            is.close();*/
			xml_things.JAXB.Items items = null;

			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(xml_things.JAXB.Items.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				items = (xml_things.JAXB.Items) jaxbUnmarshaller.unmarshal(file);                
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			List < xml_things.JAXB.Item > list = items.getItem();

			DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss",Locale.ENGLISH);
			UserDAOImpl userdao = new UserDAOImpl();
			AuctionDAOImpl auctiondao= new AuctionDAOImpl();
			CategoryDAOImpl catdao= new CategoryDAOImpl();
			int code;
			for(xml_things.JAXB.Item i : list){
				Auction auction = new Auction();
				User user = new User();
				Category category = new Category();

				if((user=userdao.find_by_name(i.getSeller().getUserID()))==null){
					user = new User();
					user.setUsername(i.getSeller().getUserID());
					user.setPassword("123");
					user.setAddress("no info");
					user.setCellPhone("no info");
					user.setCountry("who knows");
					user.setLatitude(Float.parseFloat("0.0"));
					user.setLongitude(Float.parseFloat("0.0"));
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					try {
						java.util.Date date = formatter.parse("1995-12-12");
						java.sql.Date sqlDate = new java.sql.Date(date.getTime());
						user.setDateOfBirth(sqlDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					user.setEmailAddress(i.getSeller().getUserID()+"no info");
					user.setFname("no info");
					user.setLname("no info");
					user.setPhoneNumber("00000");
					user.setTaxId("123");
					code = userdao.create(user);
					System.out.println("User: " + code);
				}
				List<Category> alist = auction.getCategories();
				for(xml_things.JAXB.Category cat : i.getCategory()){
					if((category=catdao.search_name(cat.getvalue()))==null){
						category=new Category();
						category.setName(cat.getvalue());
						category.setDesciption("no info");
						code=catdao.create(category);
					}
					alist.add(category);
				}
				for(xml_things.JAXB.Bid bid : i.getBids().getBid() ){
					if((user=userdao.find_by_name(bid.getBidder().getUserID()))==null){
						user = new User();
						user.setUsername(bid.getBidder().getUserID());
						user.setPassword("123");
						user.setAddress("no info");
						user.setCellPhone("no info");
						user.setCountry("who knows");
						user.setLatitude(Float.parseFloat("0.0"));
						user.setLongitude(Float.parseFloat("0.0"));
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						try {
							java.util.Date date = formatter.parse("1995-12-12");
							java.sql.Date sqlDate = new java.sql.Date(date.getTime());
							user.setDateOfBirth(sqlDate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						user.setEmailAddress(bid.getBidder().getUserID()+"no info");
						user.setFname("no info");
						user.setLname("no info");
						user.setPhoneNumber("00000");
						user.setTaxId("123");
						code=userdao.create(user);
					}

				}

				if(i.getName() == null){
					auction.setName("No Name");
				}else{
					auction.setName(i.getName());
				}

				if(i.getCurrently() == null){
					auction.setCurrentBid(0);
				}else{
					String s = i.getCurrently().substring(1,i.getCurrently().length());
					s=s.replace(",", "");
					System.out.println(s);
					auction.setCurrentBid(Float.parseFloat(s)); 
				}

				if(i.getBuyPrice() == null){
					auction.setBuyPrice(0);
				}else{
					String s = i.getBuyPrice().substring(1,i.getBuyPrice().length());
					s=s.replace(",", "");
					auction.setBuyPrice(Float.parseFloat(s));
				}

				if(i.getFirstBid() == null){
					auction.setFirstBid(0);
				}else{
					String s = i.getFirstBid().substring(1,i.getFirstBid().length());
					s=s.replace(",", "");
					auction.setFirstBid(Float.parseFloat(s));
				}

				if(i.getNumberOfBids() == null){
					auction.setNumberOfBids(0);
				}else{
					auction.setNumberOfBids(Integer.parseInt(i.getNumberOfBids()));
				}               

				try {
					auction.setStarted(dateFormat.parse(i.getStarted()));
					auction.setEnds(dateFormat.parse(i.getEnds()));
				} catch (ParseException ex) {
					Logger.getLogger(upload.class.getName()).log(Level.SEVERE, null, ex);
				}

				if(i.getLocation().getvalue() == null){
					auction.setItemLocation("No location");
				}else{                
					auction.setItemLocation(i.getLocation().getvalue()); 
				}

				if(i.getLocation().getLatitude() != null){
					auction.setLatitude(Float.parseFloat(i.getLocation().getLatitude()));
					auction.setLongitude(Float.parseFloat(i.getLocation().getLongitude()));
				}else{
					auction.setLatitude(0);
					auction.setLongitude(0);
				}

				if(i.getDescription().isEmpty()){      
					auction.setDescription("No description.");
				}else{          
					auction.setDescription(i.getDescription());
				}
				auction.setUser1(userdao.find_by_name(i.getSeller().getUserID()));
				auction.setCategories(alist);
				if(auctiondao.find_by_name_auction(auction.getName())==null){
					code=auctiondao.create(auction, null);
				}
				else
				{
					System.out.println("auction exists");
				}
				for(xml_things.JAXB.Bid bid : i.getBids().getBid() ){
					user=userdao.find_by_name(bid.getBidder().getUserID());
					code=auctiondao.bid_auction(user.getId(), 
							auctiondao.find_by_name_auction(i.getName()).getId(), 
							(Float.parseFloat(bid.getAmount().
									substring(1,bid.getAmount().length()))));
				}
			}
		}
		HttpSession session = request.getSession();
		session.setAttribute("progress","All items have been imported successfully!");
	}
}
