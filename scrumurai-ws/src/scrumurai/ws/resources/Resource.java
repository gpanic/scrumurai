package scrumurai.ws.resources;

import javax.ws.rs.core.Response;

public interface Resource<T> {
	public Response create(T obj); 
	public Response read(String id);
	public Response update(String id, T obj);
	public Response delete(String id);
	public Response list();
}
