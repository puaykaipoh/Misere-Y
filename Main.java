import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Main {
	
	private State game;
	private boolean player_1_AI = false;
	private boolean player_2_AI = false;
	private long P1_score = 0;
	private long P2_score = 0;
	private boolean print_end_result_only;
	
	//TODO graphic
	private Scanner in = new Scanner(System.in);
	
	public Main(int number_of_layers, boolean player_1_AI, boolean player_2_AI, long max_game, boolean continue_without_asking , boolean print_end_result_only ){
		
		this.print_end_result_only = print_end_result_only;
		
		try {
			this.game = new State(number_of_layers);
			
			this.player_1_AI = player_1_AI;
			
			this.player_2_AI = player_2_AI;
			
			last_game_state = new char[game.getBoard().length];
			
			while(true){
				
				startGame();
				
				if(!print_end_result_only) printScore();
				
				if(P1_score > Long.MAX_VALUE || P2_score > Long.MAX_VALUE || (P1_score +P2_score >= max_game)){
					
					break;
				}
				
				if(!continue_without_asking){
					
					System.out.println("Play again? Y/N");
					
					String cont = in.nextLine();
					
					while(!cont.equals("Y") && !cont.equals("N")){
						
						System.out.println("I dun understand... is it Y or N?");
						
						cont = in.nextLine();
					}
					
					if(cont.equals("N")){
						
						System.out.println("Bye!");
						
						break;
						
					}
				}
				
				reset();
				
			}
			
			if(print_end_result_only) printScore();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void printScore(){
		//TODO graphic
		//System.out.println("Player 1 score: "+P1_score);
		//System.out.println("Player 2 score: "+P2_score);
		
		System.out.println(P2_score);
	}
	
	//TODO currently AI returns random move
	private int AIMove(){
		
		char[] board_view = game.getBoard();
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int i=0; i<board_view.length; i++){
			
			if(board_view[i] =='\0'){
				
				list.add(i);
			}
		}
		
		Random random = new Random();
		
		return list.get(random.nextInt(list.size()));
	}
	
	//this variable is for improved AI's use
	char[] last_game_state;
	private int ImprovedAIP2Move(){
		
		//gets the new move by player 1
		int move_index = 0;
		
		for(int i=0; i<game.getBoard().length; i++){
			
			if(last_game_state[i] != game.getBoard()[i]){
				
				last_game_state[i] = game.getBoard()[i];
				
				if(last_game_state[i] == 'w'){
					
					move_index = i;
				}
			} 
		}
		
		//check if the new entry is in the centerline
		double center_index = (Math.sqrt(2*move_index +1) / 2) - 0.5;
		if( Math.abs(center_index - Math.floor(center_index)) < 0.000001 ) {// is it close to integer, then it is centerline
			
			//TODO choose a better move, currently is random...
			return AIMove();
			
		}else{
			
			int layer_index = game.getLayerIndex(move_index+1);
			
			int last_index_in_layer = (layer_index * (layer_index + 1)) / 2 - 1;
			
			int first_index_in_layer = (last_index_in_layer - layer_index) +1 ;
			
			double center = (last_index_in_layer + first_index_in_layer) /2.0;
			
			int desired_move = (int) (move_index - ((move_index - center) * 2));
			
			//check if move is occupied
			
			if(game.getBoard()[desired_move] == '\0') return desired_move;//TODO store move if it is not valid
			
		}
		
		return AIMove();
		
	}
	
	private void printGameState(){
		
		if(!print_end_result_only){
			
			game.printState();
		}
	}
	
	private int getP1Move(){
		
		if(this.player_1_AI){
			
			int move = AIMove();
			
			if(!print_end_result_only) System.out.println("Player 1 : "+move);
			
			return move;
		}else{
			
			int move = Integer.parseInt(in.nextLine());
			
			printGameState();
			
			while (move<0 || move >= game.getNumHex()) {
				
				System.out.println("Invalid Move");
				
				move = Integer.parseInt(in.nextLine());
			}
			
			return move;
			
		}
	}
	
	private int getP2Move(){
		
		if(this.player_2_AI){
			
			//int move = AIMove();
			
			int move = ImprovedAIP2Move();
			
			if(!print_end_result_only) System.out.println("Player 2 : "+move);
			
			return move;
		}else{
			
			//TODO check input is integer
			int move = Integer.parseInt(in.nextLine());
			
			printGameState();
			
			while (move<0 || move >= game.getNumHex()) {
				
				System.out.println("Invalid Move");
				
				move = Integer.parseInt(in.nextLine());
			}
			
			return move;
			
		}
	}
	
	private void startGame(){
		
		while(game.hasEnded() == 'e'){
			
			if(game.isPlayer1Turn()){
				
				int move = getP1Move();
				
				game.makeMove(move);
				
			}else{
				
				int move = getP2Move();
				
				game.makeMove(move);
				
			}
			
			printGameState();
			
		}
		
		if(game.hasEnded() == 'b'){
			
			if(!print_end_result_only)System.out.println("WINNER IS PLAYER 2");
			
			P2_score++;
			
		}else{
			
			if(!print_end_result_only)System.out.println("WINNER IS PLAYER 1");
			
			P1_score++;
		}
	}
	
	private void reset() throws Exception{
		
		game = new State(game.getNumLayers());
	}
	
	public static void main(String[] args){
		
		//int number_of_layers, boolean player_1_AI, boolean player_2_AI, long max_game, boolean continue_without_asking , boolean print_end_result_only 
		
		new Main(5,false, true, 1024, false, false);
		//for(int i=0; i<256; i++)
		//	new Main(10, true, true, 1024,true, true);
	}

}
