package scrumurai.data.entities;

import javax.persistence.*;

import org.datanucleus.api.jpa.annotations.Extension;

@Entity
public class Project implements EntityObject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;

	private String name;
	private String description;
	private int velocity;
	
	@ManyToOne()
	private User product_owner;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public User getProduct_owner() {
		return product_owner;
	}

	public void setProduct_owner(User product_owner) {
		this.product_owner = product_owner;
	}


}
