package pojo;

import utilities.JacksonUtils;

import java.io.IOException;

public class Data {

    private int id;
    private String employee_name;
    private int employee_salary;
    private int employee_age;
    private String profile_image;

    public Data(){

    }

    public Data(int id) throws IOException {
        Data[] employees = JacksonUtils.deserializeJson("employees.json", Data[].class);
        for (Data e:employees) {
            if(e.getId()==id){
                this.id=id;
                this.employee_name=e.getEmployee_name();
                this.employee_salary=e.getEmployee_salary();
                this.employee_age=e.getEmployee_age();
                this.profile_image=e.getProfile_image();
                break;
            }
        }
    }


    public int getId() {
        return id;
    }



    public String getEmployee_name() {
        return employee_name;
    }



    public int getEmployee_salary() {
        return employee_salary;
    }


    public int getEmployee_age() {
        return employee_age;
    }


    public String getProfile_image() {
        return profile_image;
    }


}
