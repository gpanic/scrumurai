package scrumurai.ws.resources;

import javax.ws.rs.core.Response;

public interface Resource<T> {
	public Response create(T obj); 
	public Response read(int id);
	public Response update(int id, T obj);
	public Response delete(int id);
	public Response list();
}
