@test
Feature: B-00001 - Complete flow in StackOverflow

Test Description: As a user, I will login to SO, check my account, verify my user name and logout.

Background: 
	Given I Open StackOverFlow application
	
Scenario:  Sc_02 - REST and Web Integration
  		Then I click on 'loginElement'
  		Then I click on 'userTab'
  		Then Call REST service with param
  		| 1 |
  		Then Find from REST call
  		

	