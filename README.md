## Functions needed
### mono and flux ---- publishers
for single data -> mono
for multiple data -> flux

### flux.Merge and concat
Merge will merge elements from multiple publishers
Concat aldo does the same. but when there is a delay, concat will wait for all the data from first flux
then it will move to next flux. whereas merge will wait for all the data from all the fluxes (at the same time)

### flux.zip
Adds two fluxes together and returns a flux of tuples from each of the fluxes. If one of the fluxes is empty, then 
there wont be any output even if the other flux is not empty.

Can zip a flux and mono into a flux, but not that useful as mono has only 1 item while flux can have multiple

### CollectList
Collects all the data from the flux into a list and returns all together

### Block
instead of subscribing to a flux, we can use block to get the data from the flux. So non blocking codeflow will be 
converted to blocking sync codeflow.
Issue is when there is a delay, program will be blocked till all the data is available.
Hence avoid block calls as much as possible

### Buffer
Collect set of items from publisher and sent them together. Collectlist will collect all the items into a list and 
send them together. Buffer will collect items into multiple lists (depending on buffer size) and send them together. 
Buffering can also be done with time => after specified time, it will return all collected items


## Error (exception) handling
### flux.onErrorContinue
This operator will continue the execution of the flux, even if there is an error. when there is an error, we can do 
smth like log the error and continue the execution of the flux.

### flux.onErrorReturn
This operator will return some fallback(value can be specified) value, if there is an error. We can provide 
separate fallback value for each class of exceptions.

### flux.onErrorResume
When there is an error, we can use another flux (some flux to be be used when there is an error).
It will do execution of the flux provided instead.

### flux.onErrorMap
We can convert one exception to another exception.

## Database - reactive programming
We will use mongodb, using reactive programming

## Backpressure
Provides flow control 
    extra requests handled gracefully --- extras stored in buffer and processed later

- Allows system to control pace of data flow based on capacity of the consumer - preventing overloading consumer
- Ensures that producer doesn't produce more data than consumer can handle
- Helps in system stability

## Why reactive programming?
- No blocking wait => threads can be used for other tasks => fewer threads
- lesser resources required
- Ideal for microservices often deployed on cloud / container environments
- better for streaming data - seamlessly interact with reactive programming libs
- Efficient for handling large data streams
- Real time data processing



## Thanks
Possible because of https://youtu.be/y3ySZkSgWik