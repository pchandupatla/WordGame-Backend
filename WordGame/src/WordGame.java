import java.io.File;
import java.io.IOException;
import java.util.*;
public class WordGame
{
    private Map<String,Set<String>> dictionary = new HashMap<>();
    Set<String> solutionSet = new TreeSet<>();
    Set<String> guessedAnswers;
    public WordGame() throws IOException
    {
        Scanner dict = new Scanner(new File("dictionary.txt")); //accesses dictionary file
        while(dict.hasNextLine()) { //while the file has more lines run the code for each line in the file
            String original = dict.nextLine(); //saves the original word to a variable
            if (original.length() >= 3 && original.length() <= 6) { //words only used if the length is greater than 2 and less than 7
                String key = getKey(original);
                Set<String> storedSolutions;
                if (dictionary.get(key) != null)
                {
                    storedSolutions = dictionary.get(key);
                }
                else
                {
                    storedSolutions = new TreeSet<>();
                }
                storedSolutions.add(original);
                dictionary.put(key, storedSolutions);
            }
        }
    }

    private String getKey(String a)//gets alphabetized key off of user input of random string
    {
        ArrayList<Character> alphabetize = new ArrayList<>();
        char[] input = a.toLowerCase().toCharArray();

        for (int i = 0; i < input.length; i++)
        {
            alphabetize.add(input[i]);
        }

        Collections.sort(alphabetize);
        Iterator<Character> it = alphabetize.iterator();
        StringBuilder key = new StringBuilder();
        while (it.hasNext())
        {
            key.append(it.next());
        }
        return key.toString();
    }

    private void findAnswers()//used in programming to debug solutions(finds all proper solutions to an inputted string)
    {
        Scanner kb = new Scanner(System.in);
        while(true)
        {
            System.out.println("Enter STOP to stop running");
            System.out.print("Enter what string you would like to see the combinations for: ");
            String unravel = kb.next();
            if(unravel.equals("STOP"))
            {
                break;
            }
            System.out.println(this.findAll(unravel));
            solutionSet = new TreeSet<>();
        }
    }

    private Set<String> findAll(String input)//finds all words that can be gotten from unscrambling six-letter inputted string
    {
        if(dictionary.get(this.getKey(input)) != null)
        {
            solutionSet.addAll(dictionary.get(this.getKey(input)));
        }

        for (int i = 0; i <input.length() ; i++) //recursive loop to find 3 to 6 letter words
        {
            if(input.length() == 3)
            {
                break;
            }
            StringBuilder output = new StringBuilder(input);
            output.deleteCharAt(i);
            if(dictionary.get(this.getKey(output.toString())) != null)
            {
                solutionSet.addAll(dictionary.get(this.getKey(output.toString())));
            }
            findAll(output.toString());
        }
        return solutionSet;
    }

    private void printStatements(int guessCounter, int hintCounter,int sizeStatic, Set<String> guessedAnswers)//abstraction for loading all post-game print statements
    {
        if(guessCounter==sizeStatic&&hintCounter==0)
        {
            System.out.println("Thanks for Playing! You got "+guessedAnswers.size() +" of the " +
                    ""+ sizeStatic+" words using "+guessCounter+" guess[es] and "+hintCounter+" hint[s]!" +
            "\nCongrats on your perfect score!" );
        }
        else
        {
            if(hintCounter==3)
            {
                System.out.println("Thanks for Playing! You got "+guessedAnswers.size() +" of the " +
                        ""+ sizeStatic+" words using "+guessCounter+" guess[es] and "+hintCounter+" hint[s]!");
                System.out.println("Wow all of the hints... You really needed help huh" );
            }
            else
            {
                System.out.println("Thanks for Playing! You got "+guessedAnswers.size() +" of the " +
                        ""+ sizeStatic+" words using "+guessCounter+" guess[es] and "+hintCounter+" hint[s]!" );
            }
        }
    }

    private Set<String> returnUnusedAnswers(String a)//for hint and already guessed answers systems, sets of already guessed answers must be filtered out
    {
        Set<String> compare = new TreeSet<>(this.findAll(a));
        for(String compareString : guessedAnswers)
        {
            compare.remove(compareString);
        }
        return compare;
    }

    public void runGame() //puts everything together in one cohesive game
    {
        System.out.println("WELCOME TO [INSERT TITLE HERE], THE GAME THAT'S NOT THAT FUN, BUT YOU'RE BORED, SO WHATEVER!");
        System.out.println("HOW TO PLAY: \n You will be prompted to enter a 6-letter string of characters into the console \n" +
                " Once you have done so, you are given the amount of possible words that exist with any configuration of that string \n" +
                " Your job is to guess all the possible word configurations that exist within your chosen random 6-letter String \n");
        Scanner kb = new Scanner(System.in);

        System.out.print("Enter 6-letter String to Play: ");
        String unscramble = kb.next().toLowerCase();
        while(unscramble.length() != 6)
        {
            System.out.println("Sorry, that string is not 6 characters long! Try another string.");
            unscramble = kb.next().toLowerCase();
        }

        int size = this.findAll(unscramble).size();
        while(size ==0)//searching for correct string to input
        {
            System.out.println("Sorry, that string doesn't have any good answers! Enter a new String :");
            unscramble = kb.next().toLowerCase();
            size = this.findAll(unscramble).size();
        }
        System.out.println("There are "+ size+" possible unscrambled words to guess!\nTips: Enter STOP at any time to quit! " +
        "Enter HINT at any time for a quick hint (You only have three so use them wisely)! Enter words in lower-case. Have fun!");
        int sizeStatic = size;
        System.out.println("Your scrambled string is "+ unscramble+ " ...Now unscramble!");
        System.out.println("");
        guessedAnswers = new TreeSet<>();
        int hintCounter = 0;
        int guessCounter = 0;
        while(size>0)//begins game
        {
            System.out.print("Enter guess: ");
            String userInput = kb.next();
            if(userInput.equals("STOP"))
            {
                break;
            }

            if(userInput.equals("upupdowndownleftrightleftrightbastart")||userInput.equals("toohardgimmeanswers"))//fun cheat codes
            {
                Set<String> cheatSet = this.returnUnusedAnswers(unscramble);

                System.out.print("You are the chosen one!\n");
                for(String output: cheatSet)
                {
                    System.out.print(output+" ");
                }
                System.out.println();
                continue;
            }
            if(userInput.equals("HINT"))//hint system, returns first letter, last letter, and length of unguessed unscrambled world
            {
                if(hintCounter == 3)
                {
                    System.out.println("Sorry! You have already used all of your 3 hints!");
                }else
                {
                    hintCounter++;
                    Set<String> hintSet = this.returnUnusedAnswers(unscramble);
                    Iterator it = hintSet.iterator();
                    String hint = it.next().toString();
                    System.out.println("HINT: Try a "+hint.length()+" letter word that begins with "+hint.charAt(0)+" " +
                            "and ends with "+hint.charAt(hint.length()-1));
                            System.out.println();
                }
            }
            else
            {
                if(guessedAnswers.contains(userInput))//cannot submit already guessed answers
                {
                    System.out.println("You've already guessed that correctly! Try Again!");
                    System.out.println("");
                }
                else
                {
                    if(this.findAll(unscramble).contains(userInput))
                    {
                        guessCounter++;//counts number of guesses
                        size--;
                        guessedAnswers.add(userInput);
                        if(size == 0)//if all solutions have been found
                        {
                            break;
                        }
                        System.out.println("Good Job! Only "+size+ " word(s) left!");
                        System.out.println("You have correctly guessed these words: " + guessedAnswers);
                        System.out.println("Reminder: "+ unscramble+ " is your unscrambled word");
                        System.out.println();
                    }
                    else
                    {
                        guessCounter++;
                        System.out.println("Incorrect Word! Try again!");
                        System.out.println();
                    }
                }
            }
        }
        printStatements(guessCounter,hintCounter,sizeStatic,guessedAnswers);//relies on previous method to output correct strings
        if(size>0)
        {
            //returning unguessed answers at the the end of the game
            Set<String> unguessedAnwers = this.returnUnusedAnswers(unscramble);
            System.out.println("The words you didn't get were "+unguessedAnwers);
        }
        System.out.printf("\n\n%200s","©Nobody...Steal this");
    }

    public String toString()
    {
        return dictionary.toString();
    }
}


