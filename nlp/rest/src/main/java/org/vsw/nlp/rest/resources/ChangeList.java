package org.vsw.nlp.rest.resources;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONWithPadding;
/**
 *
 * @author japod
 */
@Path("/changes")
public class ChangeList {

	static final List<ChangeRecordBean> changes = new LinkedList<ChangeRecordBean>();

	static {
		changes.add(new ChangeRecordBean(false, 2, "title \"User Guide\" updated"));
		changes.add(new ChangeRecordBean(true, 1, "fixed metadata"));
		changes.add(new ChangeRecordBean(false, 91,"added index"));
		changes.add(new ChangeRecordBean(false, 650,"\"Troubleshoothing\" chapter"));
		changes.add(new ChangeRecordBean(false, 1,"fixing typo"));
	}

	@GET
	@Produces({"application/x-javascript", "application/json"})
	public JSONWithPadding getChanges(@QueryParam("callback") String callback, @QueryParam("type") int type) {
		return new JSONWithPadding(new GenericEntity<List<ChangeRecordBean>>(changes){}, callback);
	}

	@GET @Path("latest")
	@Produces({"application/x-javascript", "application/json"})
	public JSONWithPadding getLastChange(@QueryParam("callback") String callback, @QueryParam("type") int type) {
		return new JSONWithPadding(changes.get(changes.size() - 1), callback);
	}
}