# Daret Management System

This project is an online application designed for managing and organizing Tontine operations (Darets) among a group of individuals. The objective is to facilitate collaborative financial operations where a group of people collectively save money without involving traditional banking institutions. Each Daret operation consists of multiple periods, during each of which one member of the group receives a predefined amount from all other members.

## Project Overview

The application allows users to create, organize, and manage Tontine operations. Key features include:

- **Operation Creation**: Admins can create new Daret operations specifying details such as the number of participants, start date, periodicity, amount per period, and rotation order.
- **User Management**: User authentication and management functionalities.
- **Participation Management**: Managing user participation in Daert operations.
- **Rotation Order Management**: Defining and managing the rotation order for receiving funds in each period.

## Functional Modules

The system comprises the following functional modules:

1. **Connection Management**: Handles user authentication and session management.
2. **User Management**: Allows administrators to manage user accounts.
3. **Participation Management**: Facilitates adding users to Daret operations and managing their contributions.
4. **Rotation Order Management**: Defines and manages the rotation order for fund distribution.

## Participant View

Participants can access information about the Daret operation they are engaged in, including:

- Who is currently scheduled to receive funds
- Number of periods elapsed
- Number of remaining periods
- Payment status for the current period

## Additional Features

The application supports the following additional features:

- **Equal Contributions**: Multiple users can contribute the same amount to a Daret operation.
- **User Doubling**: A user can take on the role of two or more  members in the group.

## Example Scenario

Suppose an admin creates a Daert operation named "Daret_10000" with the following parameters:

- 10 participants
- Periodic amount: 1000 DH
- Monthly periodicity
- Rotation order determined by fifo

It's possible for the "Daret_10000" operation to have more than 10 participants. For instance, if there are 11 participants, two individuals may contribute 500 DH each. Alternatively, if there are only 9 participants, one participant may contribute 2000 DH per period.

---

