package muhammadatta.employeemanager.myexceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){

        super(message);
    }

}
