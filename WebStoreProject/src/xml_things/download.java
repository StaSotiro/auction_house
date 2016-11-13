package xml_things;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import daos.AuctionDAOImpl;
import model.Auction;
import model.Bid;
import model.Category;

/**
 * Servlet implementation class download
 */
@WebServlet(name="download",urlPatterns={"/download"})
public class download extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	/* public download() {
        super();
        // TODO Auto-generated constructor stub
    }*/

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Object requestobject = request.getParameter("auction_name");
		response.setContentType("text/html;charset=UTF-8"); 
		if(requestobject!=null){
			xml_things.JAXB.ObjectFactory of = new xml_things.JAXB.ObjectFactory();

			String name= (String) requestobject;
			//System.out.println("DOWNLOAD SERVLET NAME " + name);

			/*String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
			response.setHeader(headerKey, headerValue);
			response.setContentLength((int)file.length());*/
			AuctionDAOImpl auctiondao = new AuctionDAOImpl();
			Auction auction = auctiondao.find_by_name_auction(name);

			xml_things.JAXB.Items items = of.createItems();
			List itemslist = items.getItem();

			xml_things.JAXB.Item newitem = of.createItem();
			newitem.setItemID("" + auction.getId());
			newitem.setName(auction.getName());
			newitem.setCurrently("" + auction.getName());
			newitem.setBuyPrice(""+ auction.getBuyPrice());
			newitem.setFirstBid(""+ auction.getFirstBid());
			newitem.setNumberOfBids("" + auction.getNumberOfBids());
			newitem.setCountry(auction.getItemLocation());

			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss",Locale.ENGLISH);

			newitem.setStarted(dateFormat.format(auction.getStarted()));
			newitem.setEnds(dateFormat.format(auction.getEnds()));
			newitem.setDescription(auction.getDescription());

			List<Category> categories = auction.getCategories();
			List jaxbcatlist = newitem.getCategory();
			for(Category j: categories){
				xml_things.JAXB.Category jaxbcat = of.createCategory();
				jaxbcat.setvalue(j.getName());
				jaxbcatlist.add(jaxbcat);
			}

			xml_things.JAXB.Bids jaxbids = of.createBids();
			List jaxbidlist = jaxbids.getBid();
			for(Bid j : auction.getBids()){
				xml_things.JAXB.Bidder jaxbidder = of.createBidder();
				jaxbidder.setUserID(j.getUser().getUsername());
				jaxbidder.setRating("" + j.getUser().getRating());
				jaxbidder.setCountry(j.getUser().getCountry());
				/*
				 JAXB.Location location = of.createLocation();
                location.setvalue(bidder.getCity());
                jaxbidder.setLocation(location);
				 */
				xml_things.JAXB.Bid jaxbid = of.createBid();
				jaxbid.setAmount("" + j.getValue());
                jaxbid.setTime(dateFormat.format(j.getTime()));
                jaxbid.setBidder(jaxbidder);
                jaxbidlist.add(jaxbid);  
			}
			newitem.setBids(jaxbids);
			
			
			xml_things.JAXB.Location location = of.createLocation();
            location.setLongitude("" + auction.getLongitude());
            location.setLatitude("" + auction.getLatitude());
            location.setvalue(auction.getItemLocation());
            newitem.setLocation(location);
            
            
            xml_things.JAXB.Seller seller = of.createSeller();
            seller.setUserID(auction.getUser1().getUsername());
            seller.setRating("" + auction.getUser1().getRating());
            newitem.setSeller(seller);
            
            itemslist.add(newitem);
            ////////////////////////////////////////////
            try {
                javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(items.getClass().getPackage().getName());
                javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
                marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); 
                marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                
                File file = new File(name+".xml");
                marshaller.marshal( items, file );
                
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
                response.setHeader(headerKey, headerValue);
                
                OutputStream outStream = response.getOutputStream();
                FileInputStream inStream = new FileInputStream(file);
                
                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                inStream.close();
                outStream.close(); 
                file.delete(); 
            } catch (javax.xml.bind.JAXBException ex) {
                java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); 
            }
            
		}


		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
