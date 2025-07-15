package org.banking.service.model;

/**
 * Request model for updating an existing user.
 */
public class UpdateUserRequest {
    private String name;
    private Address address;
    private String phoneNumber;
    private String email;

    /**
     * Default constructor.
     */
    public UpdateUserRequest() {}

    /**
     * Gets the user's name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's address.
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the user's address.
     * @param address the address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Gets the user's phone number.
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number.
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the user's email address.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     * @param email the email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Address model for user address details.
     */
    public static class Address {
        private String line1;
        private String line2;
        private String line3;
        private String town;
        private String county;
        private String postcode;

        /**
         * Default constructor.
         */
        public Address() {}

        /**
         * Gets the first line of the address.
         * @return the first line
         */
        public String getLine1() {
            return line1;
        }

        /**
         * Sets the first line of the address.
         * @param line1 the first line
         */
        public void setLine1(String line1) {
            this.line1 = line1;
        }

        /**
         * Gets the second line of the address.
         * @return the second line
         */
        public String getLine2() {
            return line2;
        }

        /**
         * Sets the second line of the address.
         * @param line2 the second line
         */
        public void setLine2(String line2) {
            this.line2 = line2;
        }

        /**
         * Gets the third line of the address.
         * @return the third line
         */
        public String getLine3() {
            return line3;
        }

        /**
         * Sets the third line of the address.
         * @param line3 the third line
         */
        public void setLine3(String line3) {
            this.line3 = line3;
        }

        /**
         * Gets the town of the address.
         * @return the town
         */
        public String getTown() {
            return town;
        }

        /**
         * Sets the town of the address.
         * @param town the town
         */
        public void setTown(String town) {
            this.town = town;
        }

        /**
         * Gets the county of the address.
         * @return the county
         */
        public String getCounty() {
            return county;
        }

        /**
         * Sets the county of the address.
         * @param county the county
         */
        public void setCounty(String county) {
            this.county = county;
        }

        /**
         * Gets the postcode of the address.
         * @return the postcode
         */
        public String getPostcode() {
            return postcode;
        }

        /**
         * Sets the postcode of the address.
         * @param postcode the postcode
         */
        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }
    }
} 