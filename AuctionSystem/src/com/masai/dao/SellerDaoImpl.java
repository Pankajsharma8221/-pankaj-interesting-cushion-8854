package com.masai.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.masai.Exception.ProductException;
import com.masai.Exception.SellerException;
import com.masai.bean.Product;
import com.masai.bean.Seller;
import com.masai.utility.DBUtil;

public class SellerDaoImpl implements SellerDao {

	@Override
	public Seller SellerLogin(String username, String password) throws SellerException {
	
		Seller seller=null;
		
		
		try(Connection conn =DBUtil.provideConnection()) {
			
			PreparedStatement ps=conn.prepareStatement("select * from where email =?");
			
			ps.setString(1, username);
			
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				PreparedStatement ps2=conn.prepareStatement("select * from Seller where password=?");
				
				ps2.setString(1, password);
				
				ResultSet rs2=ps2.executeQuery();
				
				if(rs2.next()) {
					int id = rs2.getInt("sellerId");
					String name = rs2.getString("name");
					String email = rs2.getString("email");
					String mobile = rs2.getString("mobile");
					String address = rs2.getString("address");
					String pass = rs2.getString("password");
					
					seller = new Seller(id, name, email, mobile, address, password);
				}else {
					throw new SellerException("Invalid password, please enter the valid password");
					
				}
			}else {
				throw new SellerException("Invalid username enter the valid username");
				
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			throw new SellerException();
		}
		
		
		
		return seller;
	}

	@Override
	public String Addproduct(int sellerid, int serialno) throws SellerException {
	String message="Not inserted";
		
		try(Connection conn= DBUtil.provideConnection()) {
		PreparedStatement ps=	conn.prepareStatement("select * from Seller where SellerId=?");
		
		ps.setInt(1,sellerid);
		
		ResultSet rs= ps.executeQuery();
		
		if(rs.next()) {
			PreparedStatement ps2=	conn.prepareStatement("select * from Product where serialno=?");
			ps2.setInt(1,serialno);
			
			ResultSet rs2= ps2.executeQuery();
			
			if(rs2.next()) {
				PreparedStatement ps3=	conn.prepareStatement("insert into Sellerproduct values(?,?)");
				
				ps3.setInt(1,sellerid);
				ps3.setInt(2, serialno);
				
				int x= ps3.executeUpdate();
				
				if(x>0) {
				message="Seller Product Added succesfully";
				}else {
				throw new SellerException("Invalid Seller id");
				
				}
				
				
			}
		}else {
			throw new SellerException("Invalid buyer id");
		}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return message;
	
	}


    @Override
    public String RegisterSeller(Seller seller) {
        
        String message = "";
        
        try(Connection conn = DBUtil.provideConnection()){
            
            PreparedStatement ps = conn.prepareStatement("insert into seller (name, email, mobile, address, password) values(?, ?, ?, ?, ?)");
            
            ps.setString(1, seller.getName());
            ps.setString(2, seller.getEmail());
            ps.setString(3, seller.getMobile());
            ps.setString(4, seller.getAddress());
            ps.setString(5, seller.getPassword());
            
            int x = ps.executeUpdate();
            
            if(x>0)
                message = "Seller registered successfully...";
            else
                message = "Seller could not be registered...";
                throw new SQLException();
                
            
        }
        catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
        return message;
    }

    @Override
    public String AddProducttoSell(Product product) {
        String message = "";
        
        try(Connection conn = DBUtil.provideConnection()){
            
            PreparedStatement ps = conn.prepareStatement("insert into product (ProductName, ProductCategory, ProductPrice, ProductQuantity) values(?, ?, ?, ?)");
            
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getProductCategory());
            ps.setInt(3, product.getProductPrice());
            ps.setInt(4, product.getProductQuantity());
            
            int x = ps.executeUpdate();
            
            if(x>0)
                message = "Product added into the Selling items list...";
            else
                message = "Product cannot be added into the Selling items list.";
                
        }
        catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
        return message;
    }

  
    @Override
    public String UpdateProductinList(Product product) {

        String message = "";
        
        try(Connection conn = DBUtil.provideConnection()){
            
            PreparedStatement ps = conn.prepareStatement("update product set ProductName = ?, ProductCategory = ?, ProductPrice = ?, ProductQuantity = ? where productId = ?");
            
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getProductCategory());
            ps.setInt(3, product.getProductPrice());
            ps.setInt(4, product.getProductQuantity());
            ps.setInt(5, product.getProductId());
            
            
            int x = ps.executeUpdate();
            
            if(x>0)
                message = "Update completed successfully...";
            else
                throw new SQLException("Product with productId " + product.getProductId() + " is not present in the list, please enter correct id");
        }
        catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
        return message;
    }

    @Override
    public String RemoveProductfromList(int id) {
        String message = "";
        
        try(Connection conn = DBUtil.provideConnection()){
            
            PreparedStatement ps = conn.prepareStatement("delete from Product where productId = ?");
            
            ps.setInt(1, id);
            
            int x = ps.executeUpdate();
            
            if(x > 0)
                message = "Product removed successfully from the list";
            else
                throw new SQLException("Product is not present in the list with productId " + id);
            
        }
        catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
        return message;
    }

    @Override
    public List<Product> CreateListofProducttoSell() throws ProductException {
        
        List<Product> productList = new ArrayList<>();
        
        try(Connection conn = DBUtil.provideConnection()){
            
            PreparedStatement ps = conn.prepareStatement("select * from Product");
            
            ResultSet rs =  ps.executeQuery();
            
            while(rs.next()) {
                int id = rs.getInt("ProductId");
                String pname = rs.getString("ProductName");
                String pcategory = rs.getString("ProductCategory");
                int pprice = rs.getInt("ProductPrice");
                int pquantity = rs.getInt("ProductQuantity");
                
                Product product = new Product(id, pname, pcategory, pprice, pquantity);
                
                productList.add(product);
            }
        }
        catch (SQLException e) {
            // TODO: handle exception
            throw new ProductException(e.getMessage());
        }
        
        return productList;
    }
}
