//U10416020	³\´º¯ô

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.lang.*;
import java.util.*;

public class CardMatch extends JFrame implements ActionListener {
	//Data fields	
	private ImageIcon[] iconCard = new ImageIcon[8];	
	private ImageIcon[] iconCardArrange = new ImageIcon[16];
	private ImageIcon back = new ImageIcon("C:/Users/Eva/Desktop/CardMatch/back1.png");
	private JButton[] buttonBack = new JButton[16];
	private JButton play = new JButton("Play");
	private JButton display = new JButton("Answer");
	private JButton interrupt = new JButton("Interrupt");
	private JPanel show = new JPanel();	
	private JLabel displayTime = new JLabel();
	private SecureRandom random = new SecureRandom();
	private CheckboxGroup choose = new CheckboxGroup();
	private Checkbox all = new Checkbox("All", true,choose);
	private Checkbox blackOnly = new Checkbox("Only Black", false, choose);
	private Checkbox redOnly = new Checkbox("Only Red", false, choose);
	private int[] randomIconList = new int[16];
	private int[] randomCardList = new int[53];
	private int[] iconCardList = new int[16];
	private int[] check = new int[16];
	private int[] answer = new int[16];
	private int selectIcon = 0;
	private int selectCard;
	private int click;	
	private int match1;
	private int match2;
	private int correct;
	private int setTime = 0;
	private ActionListener timeDelay = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			buttonBack[match1].setIcon(back);
			buttonBack[match2].setIcon(back);			
		}
	};
	private Timer delay = new Timer(300,timeDelay);
	private ActionListener changeTime = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(correct < 8){
				setTime++;
				displayTime.setText("Time: " + setTime);
			}
		}
	};
	private Timer playTime = new Timer(1000,changeTime);
	
	//Constructor
	public CardMatch(){	
		//Create panel panel1 and set GridLayout
		JPanel panel1 = new JPanel(new GridLayout(8,1,10,10));

		//Display the game time
		displayTime = new JLabel("Time: " + setTime);
		
		//Add button to panel1
		panel1.add(play);
		panel1.add(interrupt);
		panel1.add(display);				
		panel1.add(all);
		panel1.add(blackOnly);
		panel1.add(redOnly);
		panel1.add(displayTime);
		
		play.addActionListener(this);
		display.addActionListener(this);
		interrupt.addActionListener(this);
		
		
		show.setLayout(new GridLayout(4,4));		
		
		for(int i = 0; i <= 15; i++){
			//Create button with the back of the card picture			
			buttonBack[i] = new JButton(back);
			buttonBack[i].addActionListener(this);
			show.add(buttonBack[i]);
		}
		
		chooseCard();
		correctCard();
		
		add(panel1, BorderLayout.EAST);
		add(show, BorderLayout.CENTER);		
	}
	
	//Method to select card picture
	public void chooseCard(){	
		//For loop to select 2 pictures from 52 card 
		for(int i = 0; i <= 7; i++){			
			//Random from 1 to 52
			cardRandom();
			
			//Prevent the random repeat and create imageIcons
			if(randomCardList[selectCard] < 1){
				iconCard[i] = new ImageIcon("C:/Users/Eva/Desktop/CardMatch/"+selectCard+".gif");
				randomCardList[selectCard]++;
			}
			
			//If the random repeat, do random until no repeat number
			else{
				do{
					cardRandom();	
					randomCardList[selectCard]++;
				}while(randomCardList[selectCard] > 1);
				iconCard[i] = new ImageIcon("C:/Users/Eva/Desktop/CardMatch/"+selectCard+".gif");
			}
		}
		
		//For loop to create button and add to panel and select card pictures
		for(int i = 0; i <= 15; i++){			
			//Random to select imageIcons
			selectIcon = random.nextInt(8);
			
			//Judge the imageIcons whether repeat more than two times
			if(randomIconList[selectIcon] < 2){
				randomIconList[selectIcon]++;
				iconCardArrange[i] = iconCard[selectIcon];				
				check[i] = selectIcon;							
			}
			else{				
				do{
					selectIcon = random.nextInt(8);
					randomIconList[selectIcon]++;					
				} while(randomIconList[selectIcon] > 2);				
				iconCardArrange[i] = iconCard[selectIcon];				
				check[i] = selectIcon;						
			}			
		}		
	}
	
	//Method to set random
	public void cardRandom(){
		if(all.getState() == true)			
			selectCard = random.nextInt(52) + 1;
		else if(blackOnly.getState() == true)
			selectCard = random.nextInt(26) + 1;
		else if(redOnly.getState() == true)
			selectCard = random.nextInt(26) + 27;
	}
	
	public void reset(){
		for(int i = 0; i <= 7; i++){
			randomCardList[i] = 0;
		}
		
		for(int j = 0; j <= 15; j++){			
			randomIconList[j] = 0;							
			check[j] = 0;							
		}
	}
	
	
	//Method to store the correct answer
	public void correctCard(){
		for(int i = 0; i <= 15; i++){
			for(int j = 0; j <= 15; j++){
				if(check[i] == check[j] && i != j)
					answer[i] = j;				
			}
		}		
	}
	
	//Method to judge whether the match is correct
	public void judgeCard(){
		//For loop to judge the match
		for(int i = 0; i <= 15; i++){
			//If the match is false, the imageIcons will delay 1 second then turn back 
			if(match1 == i && match2 != answer[i] && correct < 8){	
				delay.start();
				delay.setRepeats(false);
			}
			
			else if(match1 == i && match2 == answer[i])
				correct++;			
		}
	}	
	
	//Action performed
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == play){
			reset();
			chooseCard();
			correctCard();
			correct = 0;
			click = 0;
			setTime = 0;
			displayTime.setText("Time: " + setTime);
			for(int i = 0; i <= 15; i++)
				buttonBack[i].setIcon(back);			
		}
		
		else if(e.getSource() == display){
			for(int i = 0; i <= 15; i++)
				buttonBack[i].setIcon(iconCardArrange[i]);
			playTime.stop();			
		}
		
		else if(e.getSource() == interrupt){
			playTime.stop();
			JOptionPane.showMessageDialog(null,"Time: " + setTime + "\nClick: " + click + "\nCorrect: " + correct);
		}
		
		else{
			for(int i = 0; i <= 15; i++){
				if(e.getSource() == buttonBack[i]){	
					buttonBack[i].setIcon(iconCardArrange[i]);				
					if(click % 2 == 0){	
						click++;
						match1 = i;						
						if(click == 1)
							playTime.start();						
					}					
					else if(click % 2 != 0 && i != match1){	
						click++;
						match2 = i;						
						judgeCard();
						if(correct == 8){
							playTime.stop();
							//show on center
							JOptionPane.showMessageDialog(null, "Clear!!" + "\nTime: " + setTime + "\nClick: " + click);
						}							
					}
				}		
			}
		}		
	}	
}