package com.babel.test.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.babel.test.config.MQConfiguration;
import com.babel.test.model.Account;
import com.babel.test.model.Transaction;
import com.babel.test.model.TransactionStatus;
import com.babel.test.model.TransactionType;
import com.babel.test.repository.AccountRepository;
import com.babel.test.repository.TransactionRepository;

@Service
@EnableScheduling
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
    EmailService emailService;
	
	@Autowired
	EventService eventService;

	@Override
	public Transaction getTransactionById(Long transactionId) {
		return transactionRepository.findById(transactionId).get();
	}

	@Override
	public Transaction getTransactionByDateCreated(Timestamp createdAt) {
		return transactionRepository.findByCreatedAt(createdAt);
	}

	@Override
	public Transaction getTransactionByDateExecuted(Timestamp executedAt) {
		return transactionRepository.findByCreatedAt(executedAt);
	}

	@Override
	public List<Transaction> getTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		transactionRepository.findAll().forEach(transactions::add);
		return transactions;
	}

	@Override
	public Page<Transaction> getTransactions(Pageable pageable) {
		Page<Transaction> transactions = transactionRepository.findAll(pageable);
		return transactions;
	}

	@Override
	public Transaction insertTransaction(Transaction transaction) {		
		Account accountFromDb = selectedAccount(transaction);
		String eventMsg;
		try {
			transaction.setStatus(TransactionStatus.PROCESSING);
			System.out.println();
			emailService.sendSimpleMessage(accountFromDb.getUser().getEmail(), "Transaction Status Update", "Transaction is PROCESSING" + "Transaction Type: " + transaction.getType());
			eventMsg = accountFromDb.getUser().getEmail() + "Transaction Status Update" + "Transaction is PROCESSING" + "Transaction Type: " + transaction.getType();
			eventService.sendEventMsg(MQConfiguration.EXCHANGE, MQConfiguration.MESSAGE_ROUTING_KEY, eventMsg);
			if(checkTransaction(transaction) == true) {
				transaction.setStatus(TransactionStatus.SUCCESS);
				emailService.sendSimpleMessage(accountFromDb.getUser().getEmail(), "Transaction Status Update", "Transaction is SUCCESSFUL" + "Transaction Type: " + transaction.getType());
				eventMsg = accountFromDb.getUser().getEmail() + "Transaction Status Update" + "Transaction is SUCCESSFUL" + "Transaction Type: " + transaction.getType();
				eventService.sendEventMsg(MQConfiguration.EXCHANGE, MQConfiguration.MESSAGE_ROUTING_KEY, eventMsg);
			};
		} catch (Exception e) {
			e.printStackTrace();
			transaction.setStatus(TransactionStatus.INTERNAL_ERROR);
			emailService.sendSimpleMessage(accountFromDb.getUser().getEmail(), "Transaction Status Update", "Transaction has a INTERNAL ERROR" + "Transaction Type: " + transaction.getType());
			eventMsg = accountFromDb.getUser().getEmail() + "Transaction Status Update" + "Transaction has a INTERNAL ERROR" + "Transaction Type: " + transaction.getType();
			eventService.sendEventMsg(MQConfiguration.EXCHANGE, MQConfiguration.MESSAGE_ROUTING_KEY, eventMsg);
		}
		
		return transactionRepository.save(transaction);
	}

	private boolean checkTransaction(Transaction transaction) {	
		checkBalance(transaction);
		checkMaxWithdralAmount(transaction);
		checkTransactionType(transaction);
		
		if(transaction.getStatus().equals(TransactionStatus.FAILED) || transaction.getStatus().equals(TransactionStatus.PENDING)) {
			return false;
		}
		return true;
	}
	
	@Scheduled(cron = "0 0 10 * * *")
	private void dailyTransactionsCheck() {
		String eventMsg;
		List<Transaction> transactions = getTransactions();
		
		Timestamp currentTime = generateDate();
		for (Transaction transaction : transactions) {
			if(transaction.getExecuteAt().compareTo(currentTime) == 0) {
				Account accountFromDb = selectedAccount(transaction);
				transaction.setStatus(TransactionStatus.SUCCESS);
				emailService.sendSimpleMessage(accountFromDb.getUser().getEmail(), "Transaction Status Update", "Transaction is SUCCESSFUL" + "Transaction Type: " + transaction.getType());
				eventMsg = accountFromDb.getUser().getEmail() + "Transaction Status Update" + "Transaction is SUCCESSFUL" + "Transaction Type: " + transaction.getType();
				eventService.sendEventMsg(MQConfiguration.EXCHANGE, MQConfiguration.MESSAGE_ROUTING_KEY, eventMsg);
			}
		}
		
	}
	
	private void checkTransactionType(Transaction transaction) {
		String eventMsg;
		Account accountFromDb = selectedAccount(transaction);
		if(transaction.getType() == TransactionType.WITHDRAW) {
			Timestamp timeStamp = generateDate(); 
			transaction.setCreatedAt(timeStamp);
			transaction.setExecuteAt(timeStamp);
		}
			
		if(transaction.getType() == TransactionType.SCHEDULE_WITHDRAW) {
			Timestamp timeStamp = generateDate(); 
			transaction.setCreatedAt(timeStamp);
			
			int compareDate = timeStamp.compareTo(transaction.getExecuteAt());
			
			if(compareDate > 0) {
				transaction.setStatus(TransactionStatus.FAILED);	
				emailService.sendSimpleMessage(accountFromDb.getUser().getEmail(), "Transaction Status Update", "Transaction has FAILED" + "Transaction Type: " + transaction.getType());
				eventMsg = accountFromDb.getUser().getEmail() + "Transaction Status Update" + "Transaction has FAILED" + "Transaction Type: " + transaction.getType();
				eventService.sendEventMsg(MQConfiguration.EXCHANGE, MQConfiguration.MESSAGE_ROUTING_KEY, eventMsg);
			}else {
				transaction.setStatus(TransactionStatus.PENDING);
				emailService.sendSimpleMessage(accountFromDb.getUser().getEmail(), "Transaction Status Update", "Transaction is PENDING" + "Transaction Type: " + transaction.getType());
				eventMsg = accountFromDb.getUser().getEmail() + "Transaction Status Update" + "Transaction is PENDING" + "Transaction Type: " + transaction.getType();
				eventService.sendEventMsg(MQConfiguration.EXCHANGE, MQConfiguration.MESSAGE_ROUTING_KEY, eventMsg);
			}			
		}
	}

	@Override
	public void updateTransaction(Long transactionid, Transaction transaction) {
		Transaction transactionFromDb = transactionRepository.findById(transactionid).get();
		transactionFromDb.setComment(transaction.getComment());
		transactionFromDb.setAmount(transaction.getAmount());
		transactionFromDb.setType(transaction.getType());
		transactionFromDb.setExecuteAt(transaction.getExecuteAt());
	}

	@Override
	public void deleteTransaction(Long transactionId) {
		transactionRepository.deleteById(transactionId);
	}

	@Override
	public List<Transaction> getTransactionsByAccountNumber(int accountNumber) {
		List<Transaction> Alltransactions = new ArrayList<>();
		List<Transaction> transactions = new ArrayList<>();
		transactionRepository.findAll().forEach(Alltransactions::add);
		for (Transaction transaction : Alltransactions) {
			if(transaction.getAccount().getAccountNumber() == accountNumber){
				transactions.add(transaction);
			}
		}
		
		return transactions;
	}
	
	private Timestamp generateDate() {
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		return timeStamp;
	}
	
	private Account selectedAccount(Transaction transaction) {
		Account accountFromDb = accountRepository.getById(transaction.getAccount().getId());
		return accountFromDb;
	}

	private void checkBalance(Transaction transaction) {		
		Account accountFromDb = selectedAccount(transaction);		
		if(accountFromDb.getBalance() < transaction.getAmount()) {
			transaction.setStatus(TransactionStatus.FAILED);
		}else {
			accountFromDb.setBalance(accountFromDb.getBalance() - transaction.getAmount());
		}
	}
	
	private void checkMaxWithdralAmount(Transaction transaction) {
		Account accountFromDb = selectedAccount(transaction);
		if(accountFromDb.getMaxWithdrawalAmount() < transaction.getAmount()) {
			transaction.setStatus(TransactionStatus.FAILED);
		}
	}
	
}
