package com.example.gelda.notification.aspect;

import com.example.gelda.notification.service.NotificationService;
import com.example.gelda.transaction.dto.TransactionAmountDTO;
import com.example.gelda.transaction.dto.TransferTransactionDTO;
import com.example.gelda.user.entity.User;
import com.example.gelda.user.repository.UserRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionNotificationAspect {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    // Matches deposit method
    @Pointcut("execution(* com.example.gelda.transaction.service.TransactionService.deposit(..))")
    public void depositPointcut() {}

    // Matches withdraw method
    @Pointcut("execution(* com.example.gelda.transaction.service.TransactionService.withdraw(..))")
    public void withdrawPointcut() {}

    // Matches transfer method
    @Pointcut("execution(* com.example.gelda.transaction.service.TransactionService.transfer(..))")
    public void transferPointcut() {}

    // Trigger after deposit
    @AfterReturning("depositPointcut() && args(userId, dto)")
    public void afterDeposit(Long userId, TransactionAmountDTO dto) {
        String message = "You deposited " + dto.getAmount() + " EGP";
        notificationService.createNotification(userId, message);
    }

    // Trigger after withdraw
    @AfterReturning("withdrawPointcut() && args(userId, dto)")
    public void afterWithdraw(Long userId, TransactionAmountDTO dto) {
        String message = "You withdrew " + dto.getAmount() + " EGP";
        notificationService.createNotification(userId, message);
    }

    // Trigger after transfer
    @AfterReturning("transferPointcut() && args(senderId, dto)")
    public void afterTransfer(Long senderId, TransferTransactionDTO dto) {
        String senderMessage = "You transferred " + dto.getAmount() + " EGP to " + dto.getReceiverMobileNumber();
        notificationService.createNotification(senderId, senderMessage);

        // Notify receiver if found
        User receiver = userRepository.findByMobileNumber(dto.getReceiverMobileNumber()).orElse(null);
        if (receiver != null) {
            String receiverMessage = "You received " + dto.getAmount() + " EGP from another user.";
            notificationService.createNotification(receiver.getId(), receiverMessage);
        }
    }
}
