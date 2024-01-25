package meu.booking_rebuild_ver2.service.concretions.Passanger;

import meu.booking_rebuild_ver2.config.Constants;
import meu.booking_rebuild_ver2.exception.BadRequestException;
import meu.booking_rebuild_ver2.exception.GenericResponseExceptionHandler;
import meu.booking_rebuild_ver2.exception.NotFoundException;
import meu.booking_rebuild_ver2.model.Admin.Loyalty;
import meu.booking_rebuild_ver2.model.Passanger.Customer;
import meu.booking_rebuild_ver2.model.Status;
import meu.booking_rebuild_ver2.model.Admin.DTO.LoyaltyDto;
import meu.booking_rebuild_ver2.repository.Admin.LoyaltyRepository;
import meu.booking_rebuild_ver2.repository.Passanger.CustomerRepository;
import meu.booking_rebuild_ver2.repository.StatusRepository;
import meu.booking_rebuild_ver2.request.Passanger.CustomerRequest;
import meu.booking_rebuild_ver2.request.Passanger.UpdateCustomerRequest;
import meu.booking_rebuild_ver2.response.GenericResponse;
import meu.booking_rebuild_ver2.response.Passanger.CustomerResponse;
import meu.booking_rebuild_ver2.service.abstractions.Admin.ILoyaltyService;
import meu.booking_rebuild_ver2.service.abstractions.Passanger.ICustomerService;
import meu.booking_rebuild_ver2.service.concretions.Admin.LoyaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private LoyaltyRepository loyaltyRepository;
    @Autowired
    private ILoyaltyService loyaltyService;
    @Autowired
    private StatusRepository statusRepository;
    private static final String PHONE_NUMBER_REGEX = "^(\\+?84|0)([3|5|7|8|9])+([0-9]{8})$";
    private static final Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
    @Override
    public GenericResponse addCustomer(CustomerRequest request) throws GenericResponseExceptionHandler {

        if(!phoneValid(request.getPhone())){
            throw new GenericResponseExceptionHandler(Constants.MESSAGE_PHONE_FORMAT_WRONG);
        }
        try{
            Customer model = customerRepository.findCustomerByPhone(request.getPhone());
            if(model == null){
                Customer requestCustomer = new Customer(request.getName(), request.getPhone());
                Status status = statusRepository.findStatusById(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"));
                requestCustomer.setStatus(status);
                Loyalty loyalty = loyaltyService.getLoyaltyByPrice(0).get();
                requestCustomer.setLoyalty(loyalty);
                requestCustomer.setNumber_of_trip(0);
                customerRepository.save(requestCustomer);
                return new GenericResponse(Constants.MESSAGE_ADDED_CUSTOMER_SUCCESSFULLY);
            }
            else{
                throw new GenericResponseExceptionHandler(Constants.MESSAGE_ADD_CUSTOMER_FAILED);
            }
        }
        catch (RuntimeException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public CustomerResponse getCustomerById(UUID id) throws NotFoundException {
        try{
            Customer model = customerRepository.findCustomerById(id);
            if(model == null){
                throw new NotFoundException();
            }
            CustomerResponse response = new CustomerResponse();
            response.setName(model.getName());
            response.setId(id);
            response.setPhone(model.getPhone());
            response.setStatus(model.getStatus().getStatus());
            response.setNumberOfTrips(model.getNumber_of_trip());
            LoyaltyDto loyaltyDto = new LoyaltyDto(model.getLoyalty());
            response.setLoyalty(loyaltyDto);
            return response;
        } catch (RuntimeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    public GenericResponse updateCustomer(UUID id, UpdateCustomerRequest request) throws NotFoundException, GenericResponseExceptionHandler {
        if(!phoneValid(request.getPhone())){
            throw new GenericResponseExceptionHandler(Constants.MESSAGE_PHONE_FORMAT_WRONG);
        }
        try{
            Customer model = customerRepository.findCustomerById(id);
            if(model == null) throw new NotFoundException();
            model.setName(request.getName());
            model.setPhone(request.getPhone());
            System.out.println(request);
            Loyalty loyalty = loyaltyService.getLoyaltyByPrice(request.getTotalPaid()).get();
            model.setLoyalty(loyalty);
            Status status = statusRepository.findStatusById(UUID.fromString(request.getStatus()));
            if(status != null) model.setStatus(status);
            model.setNumber_of_trip(request.getNumberOfTrips());
            customerRepository.save(model);
            return new GenericResponse(Constants.MESSAGE_UPDATED_CUSTOMER_SUCCESSFULLY);
        }catch (RuntimeException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public GenericResponse updateCustomerByLoyalty(UUID id, UpdateCustomerRequest request) throws NotFoundException, GenericResponseExceptionHandler {
        if(!phoneValid(request.getPhone())){
            throw new GenericResponseExceptionHandler(Constants.MESSAGE_PHONE_FORMAT_WRONG);
        }
        try{
            Customer model = customerRepository.findCustomerById(id);
            if(model == null) throw new NotFoundException();
            model.setName(request.getName());
            model.setPhone(request.getPhone());
            String rank = request.getLoyaltyDto().getRank();
            UUID id_loyalty = request.getLoyaltyDto().getId();
            if(rank != null){
                Optional<Loyalty> loyalty = loyaltyService.getLoyaltyByRank(rank);
                loyalty.ifPresent(model::setLoyalty);
            }
            else if(id_loyalty != null){
                Loyalty loyalty = loyaltyService.getLoyaltyById(id_loyalty);
                model.setLoyalty(loyalty);
            }
            else{
                throw new GenericResponseExceptionHandler("The request is not valid");
            }
            Status status = statusRepository.findStatusById(UUID.fromString(request.getStatus()));
            if(status != null) model.setStatus(status);
            model.setNumber_of_trip(request.getNumberOfTrips());
            customerRepository.save(model);
            return new GenericResponse(Constants.MESSAGE_UPDATED_CUSTOMER_SUCCESSFULLY);
        }catch (RuntimeException e){
            throw new BadRequestException(e.getMessage());
        }
    }
    protected static boolean phoneValid(String phone){
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}