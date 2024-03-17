package com.example.api.DTO;

public class UserDto {

    // 식별 id
    private Long id;
    private String name;
    private String pw;

    // Default constructor
    public UserDto() {
    }

    // Constructor with parameters
    public UserDto(Long id, String name, String pw) {
        this.id = id;
        this.pw = pw;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw){
        this.pw = pw;
    }
}