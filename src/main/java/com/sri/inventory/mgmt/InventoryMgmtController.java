package com.sri.inventory.mgmt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inventory-mgr")
public class InventoryMgmtController {
	
	private final ItemRepository itemRepository;
	private final ProfitRepository profitRepository;
	
	@Autowired
	InventoryMgmtController(ItemRepository theItemRepository, ProfitRepository theProfitRepository) {
		this.itemRepository = theItemRepository;
		this.profitRepository = theProfitRepository;
		
	}

	@RequestMapping(method = RequestMethod.POST, path="/item/{name}")
	ResponseEntity<Item> create(@PathVariable String name, @RequestParam double costPrice, 
				@RequestParam double sellPrice) {
    	return  new ResponseEntity<Item>(itemRepository.save(new Item(name, costPrice, sellPrice)), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, path="/item/{name}/buy")
   	ResponseEntity<Item> updateBuy(@PathVariable String name, @RequestParam int quantity) {
    	List<Item> items =  itemRepository.findByName(name);
    	if(!CollectionUtils.isEmpty(items) && items.size()==1) {
    		Item item = items.get(0);
    		item.setQuantity(item.getQuantity()+quantity);
    		item.setValue(item.getQuantity()*item.getCostPrice());
    		return  new ResponseEntity<Item>(itemRepository.save(item), HttpStatus.OK);
    	}else {
    		return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
    	}
       
    }
    
    @RequestMapping(method = RequestMethod.PUT, path="/item/{name}/sell")
   	ResponseEntity<Item> updateSell(@PathVariable String name, @RequestParam int quantity) {
    	List<Item> items =  itemRepository.findByName(name);
    	if(!CollectionUtils.isEmpty(items) && items.size()==1) {
    		Item item = items.get(0);
    		item.setQuantity(item.getQuantity()-quantity);
    		item.setValue(item.getQuantity()*item.getCostPrice());
    		itemRepository.save(item);
    		
    		//updating profit value after this selling
    		double profitByThisSell = (item.getSellPrice()-item.getCostPrice())*quantity;
    		
    		Profit profit = profitRepository.findTop1ByOrderByIdDesc();
    		//if there are no records of profit yet, then create one
    		if(profit == null) {
    			profit = new Profit();
    		}
    		profit.setValue(profit.getValue() + profitByThisSell);
    		profitRepository.save(profit);
    		
    		return  new ResponseEntity<Item>(item, HttpStatus.OK);
    	}else {
    		return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
    	}
       
    }
    
    @RequestMapping(method = RequestMethod.GET, path="/items")
	ResponseEntity<List<Item>> getAllItems() {
    	return  new ResponseEntity<List<Item>>(itemRepository.findAll(), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path="/report")
   	ResponseEntity<Report> getReport() {
    	
    	List<Item> items =  itemRepository.findAll();
    	
    	Profit profit = profitRepository.findTop1ByOrderByIdDesc();
    	//if there are no records of profit yet, then create one
		if(profit == null) {
			profit = new Profit();
		}
		
		Report report = new Report();
		report.setItems(items);
		report.setProfit(profit);
    	
       	return  new ResponseEntity<Report>(report, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path="/item/{name}")
   	ResponseEntity<List<Item>> getItem(@PathVariable String name) {
       	return  new ResponseEntity<List<Item>>(itemRepository.findByName(name), HttpStatus.OK);
    }
        
    @RequestMapping(method = RequestMethod.DELETE, path="/item/{name}")
	ResponseEntity<String> delete(@PathVariable String name) {
    	
    	List<Item> items =  itemRepository.findByName(name);
    	if(!CollectionUtils.isEmpty(items) && items.size()==1) {
    		Item item = items.get(0);
    		
    		//updating profit value after this deleting
    		double lossByThisDelete = (item.getCostPrice())*item.getQuantity();
    		Profit profit = profitRepository.findTop1ByOrderByIdDesc();
    		//if there are no records of profit yet, then create one
    		if(profit == null) {
    			profit = new Profit();
    		}
    		profit.setValue(profit.getValue() - lossByThisDelete);
    		profitRepository.save(profit);
    		
    		itemRepository.delete(item);
    		return  new ResponseEntity<String>("Deleted", HttpStatus.OK);
    	}else {
    		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    	}
    }

}
