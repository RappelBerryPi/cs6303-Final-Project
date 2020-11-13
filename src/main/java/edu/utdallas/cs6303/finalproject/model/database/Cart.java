package edu.utdallas.cs6303.finalproject.model.database;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  private User user;

  @ManyToMany
  @JoinTable(name = "cartItems", 
    joinColumns = @JoinColumn(
      name = "cartID", 
      referencedColumnName = "id"
    ), inverseJoinColumns = @JoinColumn(
      name = "storeItemSizeID", 
      referencedColumnName = "id"
    )
  )
  private List<StoreItemSize> cartItems;

  public long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<StoreItemSize> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<StoreItemSize> cartItems) {
    this.cartItems = cartItems;
  }

  public boolean isEmpty() {
    return this.cartItems.isEmpty();
  }
}