package tp1;

public class Clean {
	
	public static String[] removePunctuation(String text){
		//String[] res = (text.replaceAll("[^a-zA-Z0-9\\s]|[dl]'", " ").toLowerCase()).split("\\s+");
		String[] res = new String[1];
		res[0] = "RIEN";
		return res;
	}
	
	public static boolean checkDictionnary(String word, String[] dic){
		int low = 0;
		int high = (dic.length) - 1;
		boolean match = false;
		while (low <= high){
			int mid = (low + high) / 2;
			if(word.compareToIgnoreCase(dic[mid]) == 0){
				match = true;
				break;
			} else if(word.compareToIgnoreCase(dic[mid]) < 0){
				high = mid - 1;
			} else if(word.compareToIgnoreCase(dic[mid]) > 0){
				low = mid + 1;
			}
		}
		return match;
	}
	/*public static void main(String args[]){
		String[] dic = new String[8];
		String text = "H*#*#)#fhdfdfjdf[\\]~323i4i90>>>....,`344 l'''d' we+-@wqe h'heue l'homme d'academie";
		dic[0] = "apple";
		dic[1] = "banana";
		dic[2] = "cherry";
		dic[3] = "dog";
		dic[4] = "eat";
		dic[5] = "food";
		dic[6] = "good";
		dic[7] = "hotel";
		
		String tab[] = Clean.removePunctuation(text);
		for(String s:tab)
			System.out.println(s);
		
		//Clean.checkDictionnary("hotel",dic);
	}*/

	public static boolean recherche(String word, String[] wordList) {
		// TODO Auto-generated method stub
		for(int i=0; i < wordList.length; i++){
			if(word.equalsIgnoreCase(wordList[i]))
				return true;
		}
		return false;
	}
}
