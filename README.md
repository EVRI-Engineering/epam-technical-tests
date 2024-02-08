# Tasks

## PUT endpoint
We would like you to implement a PUT endpoint, which will accept a json body and a courierId path parameter.
This endpoint should check that the courier exists and then update all details of an existing courier.
If the courier does not exist, a 404 response should be returned.

## GET only active couriers
The courier-service currently provides a GET endpoint for retrieving a list of all courier details.
We would like to enhance this endpoint, adding an optional boolean query param called 'isActive', which defaults to False.
If this parameter is True, the endpoint should only return couriers whom have an active flag of True.

## Release Notes
# 0.0.2-SNAPSHOT
    - Method PUT endpoint.
    - Method GET only active couriers.
    - Added integration tests.
    - Increased Java version up to 17.
    Known issues:
    - The PUT method call will either create a new resource or update an existing one due to the idempotence. The exception 
      is thrown as it was mentioned in the task, but in my opinion, it would be better to create a new entity then throw 
      an exception.
    - The transformation firstName and lastName is not fully safe operation, it would be relevant to perform the 
      transformation with https://mapstruct.org and map firstName and lastName implicitly instead of the current implementation.
    - Any validation on data types or size has not been implemented yet.
