import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class contains Calendar, Phonebook, and all other utility classes
 * Created by Sheolfire on 8/22/2016.
 */
public class Toolkit {
    private Calendar calendar;
    private StatusMessage genericFailure = new StatusMessage(-1, "Error");
    private StatusMessage dateFormatFailure = new StatusMessage(-2, "Could Not Parse Date");
    private StatusMessage noInputFailure = new StatusMessage(-3, "No Input Given");
    private StatusMessage missingInputFailure = new StatusMessage(-4, "Not enough input given");
    private StatusMessage removeByIndexParseFailure = new StatusMessage(-5, "Could not parse integer index to remove");
    private StatusMessage invalidIndexFailure = new StatusMessage(-6, "No value to remove at specified index: invalid index");
    private StatusMessage removeByNameAndDateFailure = new StatusMessage(-7, "No value with the name and date specified");
    private StatusMessage fileNotFoundFailure = new StatusMessage(-8, "Invalid file name");
    private StatusMessage ioFailure = new StatusMessage(-9, "Could not read/write from file");
    private StatusMessage extraInputFailure = new StatusMessage(-10, "Too many input parameters for given command");
    private StatusMessage unrecognizedCommandFailure = new StatusMessage(-11, "Command not recognized");
    private StatusMessage invalidInputFailure = new StatusMessage(-12, "Invalid Input");
    private StatusMessage genericSaveFailure = new StatusMessage(-13, "Save Failed");

    private StatusMessage genericSuccess = new StatusMessage(1, "Success");
    private StatusMessage genericSaveSuccess = new StatusMessage(2, "Save Succeeded");

    private ArrayList<String> input_cache;

    //a list of possible words commands can start with that will not be added to the save file during a save command
    private String[] keywordsNotToLoad = {"save", "load", "calendar|print", "cache"};

    public static DateFormat date_format = new SimpleDateFormat("MM/dd/yyyy hh:mma");
    public static DateFormat date_format_space = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    public static DateFormat date_format_simple = new SimpleDateFormat("MM/dd/yyyy");

    public Toolkit() {
        calendar = new Calendar();
        input_cache = new ArrayList<String>();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * Parses the string passed in and uses it to execute commands such as adding and removing to the calendar or other objects
     * Does not handle exiting of the program (handled in Driver)
     * @param command the string to be parsed: inputs must be separated by |
     * @return a StatusMethod either with a failed/negative result or a successful/positive result
     */
    public StatusMessage parseCommand(String command) {
        String[] command_args = command.split("\\|");
        int num_args_counted = 1;
        int num_inputs = command_args.length;
        if (command_args.length == 0)
            return noInputFailure;
        switch (command_args[0].toLowerCase()) {
            case "help":
                System.out.println("USAGE");
                System.out.println("App: calendar");
                System.out.println("");
                return genericSuccess;
            case ("calendar"):
                if (num_args_counted < num_inputs) {
                    String input_arg1 = command_args[num_args_counted].toLowerCase();
                    num_args_counted += 1;
                    switch (input_arg1) {
                        case "add":

                            if ((num_inputs - num_args_counted) == 3) {
                                Date date = null;
                                try {
                                    if (command_args[4].split(" ").length > 2) {
                                        date = (Date) date_format_space.parse(command_args[4]);
                                    } else if(command_args[4].split(" ").length > 1) {
                                        date = (Date) date_format.parse(command_args[4]);
                                    }
                                    else {
                                        date = (Date) date_format_simple.parse(command_args[4]);
                                    }
                                } catch (ParseException e) {
                                    return dateFormatFailure;
                                }
                                calendar.addEvent(command_args[2], command_args[3], date);
                                return genericSuccess;
                            } else if(num_inputs - num_args_counted < 3) {
                                return missingInputFailure;
                            } else if(num_inputs - num_args_counted > 3) {
                                return extraInputFailure;
                            }
                            break;
                        case "remove":
                            if (num_inputs - num_args_counted == 1) {
                                try {
                                    int index = Integer.parseInt(command_args[2]);
                                    if (calendar.removeEvent(index) > 0) {
                                        return genericSuccess;
                                    } else {
                                        return invalidIndexFailure;
                                    }
                                } catch (NumberFormatException e) {
                                    return removeByIndexParseFailure;
                                }
                            } else if (num_inputs - num_args_counted == 2) {
                                Date date = null;
                                try {
                                    if (command_args[3].split(" ").length > 2) {
                                        date = (Date) date_format_space.parse(command_args[3]);
                                    } else if(command_args[3].split(" ").length > 1) {
                                        date = (Date) date_format.parse(command_args[3]);
                                    }
                                    else {
                                        date = (Date) date_format_simple.parse(command_args[3]);
                                    }
                                } catch (ParseException e) {
                                    return dateFormatFailure;
                                }
                                if (calendar.removeEvent(command_args[2], date) < 0) {
                                    return removeByNameAndDateFailure;
                                }
                                return genericSuccess;
                            } else if(num_inputs - num_args_counted < 1) {
                                return missingInputFailure;
                            }
                            else if(num_inputs - num_args_counted > 2) {
                                return extraInputFailure;
                            }
                            break;
                        case "print":

                            calendar.print();
                            return genericSuccess;

                    }

                } else {
                    return missingInputFailure;
                }

                break;
            case "phonebook":
                System.out.println("Feature In Progress");
                break;
            case "load":
                //command looks either like "load|filename" or "load|filename|(true/false)" - with true/false indicating whether or not to clear the toolkit before loading the file
                if(num_inputs - num_args_counted == 1) {
                    File file = new File(command_args[1]);
                    return loadData(file);
                } else if(num_inputs - num_args_counted == 2) {
                    File file = new File(command_args[1]);
                    boolean clear_current = Boolean.parseBoolean(command_args[2]);
                    if(clear_current) {
                        this.clear();
                    }
                    return loadData(file);
                }
                else if(num_inputs - num_args_counted < 1) {
                    return missingInputFailure;
                } else if(num_inputs - num_args_counted > 2) {
                    return extraInputFailure;
                }
                break;
            case "save":
                // TODO: 8/27/2016  add save function
                if(num_inputs - num_args_counted == 1) {
                    File file = new File(command_args[1]);
                    return saveData(file);
                } else if(num_inputs - num_args_counted == 2) {
                    File file = new File(command_args[1]);
                    boolean clear_current = Boolean.parseBoolean(command_args[2]);
                    if(clear_current) {
                        this.clear();
                    }
                    return saveData(file);
                }
                else if(num_inputs - num_args_counted < 1) {
                    return missingInputFailure;
                } else if(num_inputs - num_args_counted > 2) {
                    return extraInputFailure;
                }
                break;
            case "clear":
                this.clear();
                return genericSuccess;
            case "cache":
                printInputCache();
                return genericSuccess;

            default:
                return unrecognizedCommandFailure;
        }

        return genericFailure;
    }



    public StatusMessage isValidCommand(String command) {
        String[] command_args = command.split("\\|");
        int num_args_counted = 1;
        int num_inputs = command_args.length;
        if (command_args.length == 0)
            return noInputFailure;
        switch (command_args[0].toLowerCase()) {
            case "help":
                System.out.println("USAGE");
                System.out.println("App: calendar");
                System.out.println("");
                return genericSuccess;
            case ("calendar"):
                if (num_args_counted < num_inputs) {
                    String input_arg1 = command_args[num_args_counted].toLowerCase();
                    num_args_counted += 1;
                    switch (input_arg1) {
                        case "add":

                            if ((num_inputs - num_args_counted) == 3) {
                                Date date = null;
                                try {
                                    if (command_args[4].split(" ").length > 2) {
                                        date = (Date) date_format_space.parse(command_args[4]);
                                    } else if(command_args[4].split(" ").length > 1) {
                                        date = (Date) date_format.parse(command_args[4]);
                                    }
                                    else {
                                        date = (Date) date_format_simple.parse(command_args[4]);
                                    }
                                } catch (ParseException e) {
                                    return dateFormatFailure;
                                }
                                return genericSuccess;
                            } else if(num_inputs - num_args_counted < 3) {
                                return missingInputFailure;
                            } else if(num_inputs - num_args_counted > 3) {
                                return extraInputFailure;
                            }
                            break;
                        case "remove":
                            if (num_inputs - num_args_counted == 1) {
                                try {
                                    int index = Integer.parseInt(command_args[2]);
                                    if (calendar.isRemovable(index) > 0) {
                                        return genericSuccess;
                                    } else {
                                        return invalidIndexFailure;
                                    }
                                } catch (NumberFormatException e) {
                                    return removeByIndexParseFailure;
                                }
                            } else if (num_inputs - num_args_counted == 2) {
                                Date date = null;
                                try {
                                    if (command_args[3].split(" ").length > 2) {
                                        date = (Date) date_format_space.parse(command_args[3]);
                                    } else if(command_args[3].split(" ").length > 1) {
                                        date = (Date) date_format.parse(command_args[3]);
                                    }
                                    else {
                                        date = (Date) date_format_simple.parse(command_args[3]);
                                    }
                                } catch (ParseException e) {
                                    return dateFormatFailure;
                                }
                                if (calendar.isRemovable(command_args[2], date) < 0) {
                                    return removeByNameAndDateFailure;
                                }
                                return genericSuccess;
                            } else if(num_inputs - num_args_counted < 1) {
                                return missingInputFailure;
                            }
                            else if(num_inputs - num_args_counted > 2) {
                                return extraInputFailure;
                            }
                            break;
                        case "print":
                            return genericSuccess;

                    }

                } else {
                    return missingInputFailure;
                }

                break;
            case "phonebook":
                System.out.println("Feature In Progress");
                break;
            case "load":
                //command looks either like "load|filename" or "load|filename|(true/false)" - with true/false indicating whether or not to clear the toolkit before loading the file
                if(num_inputs - num_args_counted == 1) {
                    File file = new File(command_args[1]);
                    return loadData(file);
                } else if(num_inputs - num_args_counted == 2) {
                    File file = new File(command_args[1]);
                    boolean clear_current = Boolean.parseBoolean(command_args[2]);
                    if(clear_current) {
                        this.clear();
                    }
                    return genericSuccess;
                }
                else if(num_inputs - num_args_counted < 1) {
                    return missingInputFailure;
                } else if(num_inputs - num_args_counted > 2) {
                    return extraInputFailure;
                }
                break;
            case "save":
                // TODO: 8/27/2016  add save function
                if(num_inputs - num_args_counted == 1) {
                    File file = new File(command_args[1]);
                    return saveData(file);
                } else if(num_inputs - num_args_counted == 2) {
                    File file = new File(command_args[1]);
                    boolean clear_current = Boolean.parseBoolean(command_args[2]);
                    if(clear_current) {
                        this.clear();
                    }
                    return genericSuccess;
                }
                else if(num_inputs - num_args_counted < 1) {
                    return missingInputFailure;
                } else if(num_inputs - num_args_counted > 2) {
                    return extraInputFailure;
                }
                break;
            case "clear":
                this.clear();
                return genericSuccess;
            case "cache":
                return genericSuccess;

            default:
                return unrecognizedCommandFailure;
        }

        return genericFailure;
    }
    //somehow is still saving "load" commands
    public StatusMessage saveData(File file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for(String s:input_cache) {
                if(isValidCommand(s).getStatusCode() > 0) {
                    writer.write(s);
                    writer.newLine();
                }
            }
            return genericSaveSuccess;
        } catch (FileNotFoundException e) {
            return fileNotFoundFailure;
        } catch (IOException e) {
            return ioFailure;
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    return ioFailure;
                }
            }
        }
    }


    public StatusMessage loadData(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String input;
            StatusMessage temp = null;
            //If the input is valid, attempt to parse it
            while((input = reader.readLine()) != null) {
                if(isValidToSaveAndLoad(input)) {
                    temp = parseCommand(input);
                    System.out.println(temp.getStatusMessage());
                    if(temp.getStatusCode() > 0)
                        this.addToInputCache(input);
                }

            }
        } catch (FileNotFoundException e) {
            return fileNotFoundFailure;
        } catch (IOException e) {
            return ioFailure;
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    return ioFailure;
                }
            }
        }
        return genericSuccess;
    }
    
    public boolean isValidToSaveAndLoad(String input) {
        for(String key:keywordsNotToLoad) {
            if(input.startsWith(key)) {
                return false;
            }
        }
        return true;
    }

    public void clear() {
        calendar = new Calendar();
        clearInputCache();
    }

    /**
     * If the input is valid, add it to the input_cache array to store for using in saving
     * @param addition the string to be added to the cache
     */
    public void addToInputCache(String addition) {
        if(input_cache.size() >= Integer.MAX_VALUE) {
            clearInputCache();
        }
        boolean addToCache = isValidToSaveAndLoad(addition);

        if(addToCache)
            input_cache.add(addition);

    }

    public void clearInputCache() {
        input_cache = new ArrayList<String>();
    }

    public void printInputCache() {
        for(String s:input_cache) {
            System.out.println(s);
        }
    }
}
