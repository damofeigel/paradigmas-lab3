package subscription;

import java.util.ArrayList;
import java.util.List;

/* 
* Esta clase abstrae el contenido del archivo  de suscripcion(json) 
*/
public class Subscription {
	private List<SingleSubscription> suscriptionsList;
	
	
	public Subscription(String subscriptionFilePath) {
		super();
		this.suscriptionsList = new ArrayList<SingleSubscription>();
	}

	public List<SingleSubscription> getSubscriptionsList(){
		return this.suscriptionsList;
	}

	public void addSingleSubscription(SingleSubscription s) {
		this.suscriptionsList.add(s);
	}
	
	public SingleSubscription getSingleSubscription(int i){
		return this.suscriptionsList.get(i);
	}

	public int getSubListSize(){
		return this.suscriptionsList.size();
	}

	@Override
	public String toString() {
		String str ="";
		for (SingleSubscription s: getSubscriptionsList()){
			str += s.toString();
		}
		return "[" + str + "]";
	}	
	
	public void prettyPrint(){
		System.out.println(this.toString());
	}
	
}
