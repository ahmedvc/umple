/* EXPERIMENTAL CODE - NON COMPILEABLE VERSION OF C++ */
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.16.0.2388 modeling language!*/

#include "Client.h"

// attributes on both sides of the constraint's boolean expression
	
  //------------------------
  // CONSTRUCTOR
  //------------------------
  
 Client::Client(const int & aMinAge, const int & aAge)
  {
    if ( !(aAge>minAge))
    { 
     throw "Please provide a valid age"; 
    }
    if ( !(age>aMinAge))
    { 
     throw "Please provide a valid minAge"; 
    }
    minAge = aMinAge;
    age = aAge;
  }
  
  //------------------------
  // COPY CONSTRUCTOR
  //------------------------

 Client::Client(const Client & client)
  {
    this->minAge = client.minAge;
    this->age = client.age;
  }
  	
  //------------------------
  // Operator =
  //------------------------

 Client Client::operator=(const Client & client)
  {
    this->minAge = client.minAge;
    this->age = client.age;
  }

  //------------------------
  // INTERFACE
  //------------------------

  bool Client::setMinAge(const int & aMinAge)
  {
    bool wasSet = false;
    if (age>aMinAge)
    {
    minAge = aMinAge;
    wasSet = true;
    }
    return wasSet;
  }

  bool Client::setAge(const int & aAge)
  {
    bool wasSet = false;
    if (aAge>minAge)
    {
    age = aAge;
    wasSet = true;
    }
    return wasSet;
  }

  int Client::getMinAge() const
  {
    return minAge;
  }

  int Client::getAge() const
  {
    return age;
  }

  
  //------------------------
  // DESTRUCTOR
  //------------------------
  
Client::~Client()
  {}
