package edu.utdallas.cs6303.finalproject.model.database;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Cart {

  @Id
  private long id;

  @OneToOne
  private User user;

  @ManyToMany
  @JoinTable(name = "cartItems", 
    joinColumns = @JoinColumn(
      name = "cartID", 
      referencedColumnName = "id"
    ), inverseJoinColumns = @JoinColumn(
      name = "storeItemID", 
      referencedColumnName = "id"
    )
  )
  private Collection<StoreItem> cartItems;

  public long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Collection<StoreItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(Collection<StoreItem> cartItems) {
    this.cartItems = cartItems;
  }

  public boolean isEmpty() {
    return this.cartItems.isEmpty();
  }
}