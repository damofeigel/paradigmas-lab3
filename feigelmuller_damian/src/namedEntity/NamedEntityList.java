package namedEntity;


class Person extends NamedEntity {
    private int ID;

    public Person(String name, String category, int frequency) {
        super(name, category, frequency);
	}

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}

class Place extends NamedEntity {
    public Place(String name, String category, int frequency) {
        super(name, category, frequency);
	}
}

class Organization extends NamedEntity {
    private String canon;
    private int membership;
    private String type;

    public Organization(String name, String category, int frequency) {
        super(name, category, frequency);
	}    

    public void setcanon(String canon) {
        this.canon = canon;
    }

    public void setMembership(int membership) {
        this.membership = membership;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getcanon() {
        return canon;
    }

    public int getMembership() {
        return membership;
    }

    public String getType() {
        return type;
    }

}

class Product extends NamedEntity {
    private String commercial;
    private String producer;
    
    public Product(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setCommercial(String commercial) {
        this.commercial = commercial;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCommercial() {
        return commercial;
    }
    
    public String getProducer() {
        return producer;
    }
}

class Event extends NamedEntity {
    private String canon;
    private String date;
    private boolean recurrent;

    public Event(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setcanon(String canon) {
        this.canon = canon;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRecurrent(boolean recurrent) {
        this.recurrent = recurrent;
    }

    public String getcanon() {
        return canon;
    }

    public String getDate() {
        return date;
    }

    public boolean getRecurrent() {
        return recurrent;
    }
}

class Date extends NamedEntity {
    private boolean precise;
    private String canon;

    public Date(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setPrecise(boolean precise) {
        this.precise = precise;
    }

    public void setcanon(String canon) {
        this.canon = canon;
    }

    public boolean getPrecise(){
        return precise;
    }

    public String getcanon() {
        return canon;
    }
}

class CategoryOther extends NamedEntity {
    private String comments;
    
    public CategoryOther(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }
}

class Surname extends Person {
    private String canon;
    private String origin;

    public Surname(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setCanon(String canon) {
        this.canon = canon;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCanon() {
        return canon;
    }

    public String getOrigin() {
        return origin;
    }
}

class Name extends Person {
    private String canon;
    private String origin;
    private String[] altNames;

    public Name(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setCanon(String canon) {
        this.canon = canon;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setAltNames(String[] altNames) {
        this.altNames = altNames;
    }

    public String getCanon() {
        return canon;
    }

    public String getOrigin() {
        return origin;
    }

    public String[] getAltNames() {
        return altNames;
    }
}

class Title extends Person {
    private String canon;
    private boolean proffesional;

    public Title(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setCanon(String canon) {
        this.canon = canon;
    }

    public void setProffesional(boolean proffesional) {
        this.proffesional = proffesional;
    }

    public String getCanon() {
        return canon;
    }

    public boolean getProfessional() {
        return proffesional;
    }
}

class Country extends Place {
    private int poblation;
    private String language;

    public Country(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setPoblation(int poblation) {
        this.poblation = poblation;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPoblation() {
        return poblation;
    }

    public String getLanguage() {
        return language;
    }
}

class City extends Place {
    private String country;
    private String capital; 
    private int poblation;

    public City(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setPoblation(int poblation) {
        this.poblation = poblation;
    }

    public String getCountry() {
        return country;
    }

    public String getCapital() {
        return capital;
    }

    public int getPoblation() {
        return poblation;
    }
}

class Address extends Place {
    private String city;

    public Address(String name, String category, int frequency) {
        super(name, category, frequency);
	}   

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
interface Theme {
}

interface Sports extends Theme {    
}

interface Culture extends Theme {

}

interface Politics extends Theme {

}

interface ThemesOthers extends Theme {

}

interface Football extends Sports {

} 

interface Basketball extends Sports {

}       

interface Tennis extends Sports {

}

interface Formula1 extends Sports {

}

interface SportsOthers extends Sports {

}

interface Cinema extends Culture {

}

interface Music extends Culture {

}

interface CultureOthers extends Culture {

}

interface National extends Politics {

}

interface International extends Politics {

}

interface PoliticsOthers extends Politics {
    
}



class PlaceOther extends Place {
    private String comments;

    public PlaceOther(String name, String category, int frequency) {
        super(name, category, frequency);
	}
    
    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }
}



// NE-TH
public class NamedEntityList {

    public NamedEntityList() {

    }

    public class NameFootball extends Name implements Football {

        public NameFootball(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class NameBasketball extends Name implements Basketball {

        public NameBasketball(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class NameTennis extends Name implements Tennis {

        public NameTennis(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class NameFormula1 extends Name implements Formula1 {

        public NameFormula1(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class NameCinema extends Name implements Cinema {

        public NameCinema(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class NameMusic extends Name implements Music {
        public NameMusic(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class NameNational extends Name implements National {

        public NameNational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class NameInternational extends Name implements International {
        public NameInternational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    //////////////////////////////////////

    public class SurnameFootball extends Surname implements Football {

        public SurnameFootball(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class SurnameBasketball extends Surname implements Basketball {

        public SurnameBasketball(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class SurnameTennis extends Surname implements Tennis {

        public SurnameTennis(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class SurnameFormula1 extends Surname implements Formula1 {

        public SurnameFormula1(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class SurnameCinema extends Surname implements Cinema {

        public SurnameCinema(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class SurnameMusic extends Surname implements Music {
        public SurnameMusic(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class SurnameNational extends Surname implements National {

        public SurnameNational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class SurnameInternational extends Surname implements International {
        public SurnameInternational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    //////////////////////

    public class TitleMusic extends Title implements Music {
        public TitleMusic(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class TitleNational extends Title implements National {

        public TitleNational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class TitleInternational extends Title implements International {
        public TitleInternational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class TitleOthers extends Title implements ThemesOthers {
        public TitleOthers(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    ////////////////////////////

    public class CountryNational extends Country implements National {
        public CountryNational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class CountryInternational extends Country implements International {
        public CountryInternational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class CountryCinema extends Country implements Cinema {
        public CountryCinema(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class CountryMusic extends Country implements Music {
        public CountryMusic(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class CountryCultureOthers extends Country implements CultureOthers {
        public CountryCultureOthers(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    ///////////////////////////

    public class CityNational extends City implements National {
        public CityNational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class CityCinema extends City implements Cinema {
        public CityCinema(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class CityMusic extends City implements Music {
        public CityMusic(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class CityCultureOthers extends City implements CultureOthers {
        public CityCultureOthers(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    /////////////////////////////////

    public class AddressOthers extends Address implements ThemesOthers {
        public AddressOthers(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    ///////////////////////////////

    public class OrganizationNational extends Organization implements National {
        public OrganizationNational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class OrganizationInternational extends Organization implements International {
        public OrganizationInternational(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    public class OrganizationOthers extends Organization implements ThemesOthers {
        public OrganizationOthers(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    ///////////////////////////////

    public class ProductOthers extends Product implements ThemesOthers {
        public ProductOthers(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    ///////////////////////////////

    public class DateOther extends Date implements ThemesOthers {
        public DateOther(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

    ///////////////////////////////

    public class CategoryOther extends Country implements ThemesOthers {
        public CategoryOther(String name, String category, int frequency) {
            super(name, category, frequency);
        }
    }

}
