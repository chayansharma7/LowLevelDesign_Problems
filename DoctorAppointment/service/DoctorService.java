package DoctorAppointment.service;

import DoctorAppointment.Mode.Print;
import DoctorAppointment.database.DoctorsRepository;
import DoctorAppointment.model.AvailableDoctor;
import DoctorAppointment.model.Doctor;
import DoctorAppointment.model.Specialization;
import DoctorAppointment.util.Utils;
import DoctorAppointment.model.TimeSlot;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DoctorService {

    DoctorsRepository doctorsRepository;
    Print print;

    public DoctorService(Print print, DoctorsRepository doctorsRepository){
        this.print = print;
        this.doctorsRepository = doctorsRepository;
    }

    public void registerDoctor(Doctor doctor){
        doctorsRepository.registerDoctor(doctor);
        print.printData("Welcome "+doctor.getDoctorName());
    }
    public void addAvailability(Integer doctorId, TimeSlot timeSlot){
        // Check Slot 30 mins
        LocalTime start = Utils.convertStringToTime(timeSlot.getStart());
        LocalTime end = Utils.convertStringToTime(timeSlot.getEnd());

        // finding difference between "Time"
        if(ChronoUnit.MINUTES.between(start, end) % 60 == 30){
            doctorsRepository.addAvailability(doctorId, timeSlot);
            print.printData("Done Doc!");
        } else {
            print.printData( "Sorry "+doctorsRepository.getDoctorDetails(doctorId).getDoctorName() +" slots are 30 mins only");
//            throw new SlotException(
//                    "Sorry"+doctorsRepository.getDoctorDetails(doctorId).getDoctorName() +" slots are 30 mins only"
//            );
        }

    }

    public void showAvailableSlotsBySpeciality(Specialization specialization) throws CloneNotSupportedException {
        List<Doctor> doctorsBySpeciality = doctorsRepository.getDoctorsBySpeciality(specialization);
        List<AvailableDoctor> availableTimeSlots = doctorsRepository.getAvailableTimeSlotsForAllDoctorsForSpecialization(doctorsBySpeciality);
        if(availableTimeSlots.size() > 0){
            print.printData("Available Doctors With time Slots");
            for(AvailableDoctor doctor: availableTimeSlots){
                for(TimeSlot slot: doctor.getSlotList()){
                    print.printData(doctor.getDoctor().getDoctorName()+" "+slot.getStart()+" - "+slot.getEnd());
                }
            }
        } else {
            print.printData("No slots available");
        }
    }

}
