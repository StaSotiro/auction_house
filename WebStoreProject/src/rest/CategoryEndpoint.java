package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mysql.fabric.xmlrpc.base.Array;

import daos.CategoryDAOImpl;
import model.Auction;
import model.Category;
import model.User;
@Path("/category")
public class CategoryEndpoint {
	
	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getauctions(){
		System.out.println("Listing categories");
		Response rb=null;
		String str="";
		CategoryDAOImpl catdao = new CategoryDAOImpl();
		JSONArray jsonArray = new JSONArray();
		List<Category> list = catdao.list();
		for(Category u: list){
			System.out.println(u.toString());
			JSONObject obj = new JSONObject();
			obj=u.toJSON();
			jsonArray.put(obj);
		}
		str+=jsonArray.toString();
		rb=Response.ok(str).build();
		return rb;
	}
	@Path("/add")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response add_category(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject catdata = new JSONObject(data);
		CategoryDAOImpl catdao = new CategoryDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		Category cat = new Category();
		cat.setName(catdata.optString("name"));
		cat.setDesciption(catdata.optString("description"));
		try{
			int code = catdao.create(cat);
			if(code == 200){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfully added new category");
				str=resp.toString();
			}
			else if(code==501){
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Already exists with this PK");
				str=resp.toString();
			}
			else{
				resp.put("HTTP_CODE", Integer.toString(code));
				resp.put("MSG: ", "Something went wrong with your addition, please try later");
				str=resp.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();

	}
	//get all auctions based on category
	@Path("/auctions")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response category_auctions(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject catdata = new JSONObject(data);
		CategoryDAOImpl catdao = new CategoryDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		try{
			Category cat = catdao.search_name(catdata.optString("name"));
			if(cat!=null){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", cat.getAuctions().toString());
				str=resp.toString();

			}
			else{
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Something went wrong, please try later");
				str=resp.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}

}
