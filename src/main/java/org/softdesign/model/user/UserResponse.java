package org.softdesign.model.user;

public class UserResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String maidenName;
    private int age;
    private String gender;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String birthDate;
    private String image;
    private String bloodGroup;
    private double height;
    private double weight;
    private String eyeColor;
    private Hair hair;
    private String ip;
    private Address address;
    private String macAddress;
    private String university;
    private Bank bank;
    private Company company;
    private String ein;
    private String ssn;
    private String userAgent;
    private Crypto crypto;
    private String role;


    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getImage() {
        return image;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public Hair getHair() {
        return hair;
    }

    public String getIp() {
        return ip;
    }

    public Address getAddress() {
        return address;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getUniversity() {
        return university;
    }

    public Bank getBank() {
        return bank;
    }

    public Company getCompany() {
        return company;
    }

    public String getEin() {
        return ein;
    }

    public String getSsn() {
        return ssn;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public String getRole() {
        return role;
    }

    public static class Hair {
        private String color;
        private String type;

        public String getColor() {
            return color;
        }

        public String getType() {
            return type;
        }

    }

    public static class Address {
        private String address;
        private String city;
        private String state;
        private String stateCode;
        private String postalCode;
        private Coordinates coordinates;
        private String country;

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getStateCode() {
            return stateCode;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public String getCountry() {
            return country;
        }

        public static class Coordinates {
            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public double getLng() {
                return lng;
            }

        }
    }

    public static class Bank {
        private String cardExpire;
        private String cardNumber;
        private String cardType;
        private String currency;
        private String iban;

        public String getCardExpire() {
            return cardExpire;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public String getCardType() {
            return cardType;
        }

        public String getCurrency() {
            return currency;
        }

        public String getIban() {
            return iban;
        }

    }

    public static class Company {
        private String department;
        private String name;
        private String title;
        private Address address;

        public String getDepartment() {
            return department;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public Address getAddress() {
            return address;
        }

    }

    public static class Crypto {
        private String coin;
        private String wallet;
        private String network;

        public String getCoin() {
            return coin;
        }

        public String getWallet() {
            return wallet;
        }

        public String getNetwork() {
            return network;
        }

    }
}
