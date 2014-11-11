import java.util.ArrayList;


public class State {
	
	private int number_of_layers;
	
	private char[] board;

	private boolean is_player_1_turn = true;
	
	private int number_of_hex;
	
	private WeightedQuickUnionPathCompressionUF union_find_1;
	
	private WeightedQuickUnionPathCompressionUF union_find_2;

	public State (int number_of_layers) throws Exception{
		
		if(number_of_layers>101){
			
			throw new Exception("Too big!");
		}
		
		if(number_of_layers<1){
			
			throw new Exception("Too small!");
		}
		
		this.number_of_layers = number_of_layers;
		
		this.number_of_hex = (number_of_layers)*(number_of_layers+1) / 2;
		
		this.board = new char[number_of_hex];
		
		this.union_find_1 = new WeightedQuickUnionPathCompressionUF(number_of_hex);
		
		this.union_find_2 = new WeightedQuickUnionPathCompressionUF(number_of_hex);
	} 
	
	//@return 'w' if winner is white player 1
	//@return 'b' if winner is black player 2
	//@return 'e' if game has not ended
	public char hasEnded(){
		
		int[] corners = {0, (number_of_layers)*(number_of_layers-1)/2, number_of_hex-1};
		
		for(int i=corners[1]; i< number_of_hex; i++){
			
			if(union_find_1.connected(corners[0],i)){
				
				return 'b';
			}
		}
		
		int n1=0;
		
		for(int i=2; n1<number_of_hex ;i++){
			
			if(union_find_1.connected(corners[1], n1)){
				
				return 'b';
			}
			
			n1 = n1+i;
		}
		
		int n2=0;
		
		for(int i=0; n2<=corners[1]; i++){
			
			if(union_find_1.connected(corners[2], n2)){
				
				return 'b';
			}
			
			n2 = n2+i;
		}
		
		int n3=1;
		
		for(int i=2; n3<corners[1]; i++){
			
			int m3=2;
			
			for(int j=3; m3<corners[2]; j++){
				
				if(union_find_1.connected(n3,m3)){
					
					for(int k=(corners[1]+1); k<corners[2]; k++){
						
						if(union_find_1.connected(n3, k)){
							
							return 'b';
						}
					}
				}
				
				m3 = m3+j;
			}
			
			n3 = n3+i;
		}
		
		for(int i=corners[1]; i< number_of_hex; i++){
			
			if(union_find_2.connected(corners[0],i)){
				
				return 'w';
			}
		}
		
		int n4=0;
		
		for(int i=2; n4<number_of_hex ;i++){
			
			if(union_find_2.connected(corners[1], n4)){
				
				return 'w';
			}
			
			n4 = n4+i;
		}
		
		int n5=0;
		
		for(int i=0; n5<=corners[1]; i++){
			
			if(union_find_2.connected(corners[2], n5)){
				
				return 'w';
			}
			
			n5 = n5+i;
		}
		
		int n6=1;
		
		for(int i=2; n6<corners[1]; i++){
			
			int m6=2;
			
			for(int j=3; m6<corners[2]; j++){
				
				if(union_find_2.connected(n6,m6)){
					
					for(int k=(corners[1]+1); k<corners[2]; k++){
						
						if(union_find_2.connected(n6, k)){
							
							return 'w';
						}
					}
				}
				
				m6 = m6+j;
			}
			
			n6 = n6+i;
		}
		
		
		return 'e';
	}
	
	int getNumHex(){
		
		return number_of_hex;
	}
	
	boolean isPlayer1Turn(){
		
		return is_player_1_turn;
	}
	
	char[] getBoard(){
		//TODO make a copy
		return board;

	}
	
	int getNumLayers(){
		
		return number_of_layers;
	}
	
	public void printState(){
		
		int current_layer = 1;
		
		for(int j=0; j < (this.number_of_layers - current_layer); j++){
			
			System.out.print("  ");
		}
		
		for(int i=0; i<this.number_of_hex; i++){
			
			if(this.getLayerIndex(i+1) > current_layer){
				
				System.out.println("");
				
				current_layer = this.getLayerIndex(i+1);
				
				for(int j=0; j < (this.number_of_layers - current_layer)/2; j++){
					
					System.out.print("    ");
				}
				
				if((this.number_of_layers - current_layer)%2==1){
					
					System.out.print("  ");
				}
			}
			
			if(this.board[i] == '\0'){
				
				System.out.print(i);
				
				for(int j=0; j<(4-Integer.toString(i).length()); j++){
					
					System.out.print(" ");
				}
				
			}else{
				
				System.out.print(board[i]+"   ");
			}
		}
		System.out.println("");
	}
	
	int getLayerIndex(int cell_index){
		
		double d_cell_index = cell_index+0.0;
		
		return (int) Math.ceil(Math.sqrt(1+8*d_cell_index)/2-0.5);
	}
	
	private ArrayList<Integer> getNeighbours(int cell_index){
		
		cell_index++;
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		int layer_num = getLayerIndex(cell_index);
		
		int top_left = cell_index-layer_num;
		
		if(top_left>0 && top_left <= number_of_hex && (getLayerIndex(top_left) == layer_num-1)){
			
			list.add(top_left-1);
		}
		
		int top_right = top_left+1;
		
		if(top_right>0 && top_right <= number_of_hex && (getLayerIndex(top_right) == layer_num-1)){
			
			list.add(top_right-1);
		}
		
		int right = cell_index+1;
		
		if(right<=number_of_hex && (getLayerIndex(right) == layer_num)){
			
			list.add(right-1);
		}
		
		int left = cell_index-1;
		
		if(left>0 && (getLayerIndex(left) == layer_num)){
			
			list.add(left-1);
		}
		
		int down_left = cell_index + layer_num;
		
		if(down_left>0 && down_left <= number_of_hex &&(getLayerIndex(down_left) == layer_num + 1)){
			
			list.add(down_left-1);
		}
		
		int down_right = down_left+1;
		
		if(down_right >0 && down_right <=number_of_hex && (getLayerIndex(down_right) == layer_num + 1)){
			
			list.add(down_right-1);
		}
		
		return list;
	}
	
	//makes the move and return true if valid move.
	//if move not valid does not make the move and return false
	public boolean makeMove(int square_index){
		
		if(board[square_index] != '\0') return false;
		
		if(is_player_1_turn){
			
			board[square_index] = 'w';
			
			for(int neighbour_index : getNeighbours(square_index)){
				
				if(board[neighbour_index] == 'w'){
					
					union_find_1.union(neighbour_index, square_index);
				}
			}
			
			is_player_1_turn = false;
			
		}else{
			
			board[square_index] = 'b';
			
			for(int neighbour_index : getNeighbours(square_index)){
				
				if(board[neighbour_index] == 'b'){
					
					union_find_2.union(neighbour_index, square_index);
				}
			}
			
			is_player_1_turn = true;
		}
		
		return true;
	}
	//Unit Test
	public static void main(String[] args){
		
		//Unit test
		State game;
		try {
			game = new State(6);
			
			game.makeMove(7);//white
			game.makeMove(8);//black
			
			game.makeMove(12);//white
			game.makeMove(13);//black
			
			game.makeMove(4);//white
			game.makeMove(11);//black
			
			game.makeMove(9);//white
			game.makeMove(3);//black
			
			game.makeMove(14);//white
			
			if(game.hasEnded() == 'b'){
				
				System.out.println("Winner is player 2");
			}else if(game.hasEnded() == 'w'){
				
				System.out.println("Winner is player 1");
			} else{
				
				System.out.println("Game has not ended");
			}
			
			game.makeMove(18);//black
			
			game.makeMove(10);//white
			game.makeMove(2);//black
			
			game.makeMove(17);//white
			game.makeMove(16);//black
			
			game.makeMove(6);//white
			game.makeMove(1);//black
			
			game.makeMove(15);//white
			game.makeMove(0);//black
			
			game.makeMove(19);//white
			game.makeMove(20);//black
			
			game.makeMove(5);//white, black wins
			
			
			if(game.hasEnded() == 'b'){
				
				System.out.println("Winner is player 2");
			}else if(game.hasEnded() == 'w'){
				
				System.out.println("Winner is player 1");
			} else{
				
				System.out.println("Game has not ended");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			
			game = new State(7);
			
			game.makeMove(5);
			game.makeMove(9);
			
			game.makeMove(8);
			game.makeMove(13);
			
			game.makeMove(7);
			game.makeMove(19);
			
			game.makeMove(6);
			game.makeMove(14);
			
			game.makeMove(12);//white
			game.makeMove(0);//black
			
			game.makeMove(17);//white
			game.makeMove(20);//black
			
			game.makeMove(23);//white
			
			if(game.hasEnded() == 'b'){
				
				System.out.println("Winner is player 2");
			}else if(game.hasEnded() == 'w'){
				
				System.out.println("Winner is player 1");
			} else{
				
				System.out.println("Game has not ended");
			}
			
			game = new State(7);
			
			game.makeMove(12);//white
			game.makeMove(9);//black
			
			game.makeMove(8);//white
			game.makeMove(13);//black
			
			game.makeMove(7);//white
			game.makeMove(18);//black
			
			game.makeMove(4);//white
			game.makeMove(24);//black
			
			game.makeMove(3);//white
			game.makeMove(17);//black
			
			game.makeMove(5);//white
			game.makeMove(11);//black
			
			game.makeMove(1);//white
			game.makeMove(6);//black
			
			if(game.hasEnded() == 'b'){
				
				System.out.println("Winner is player 2");
			}else if(game.hasEnded() == 'w'){
				
				System.out.println("Winner is player 1");
			} else{
				
				System.out.println("Game has not ended");
			}
		}catch (Exception e){
			
			e.printStackTrace();
		}
		
		
	}
}
