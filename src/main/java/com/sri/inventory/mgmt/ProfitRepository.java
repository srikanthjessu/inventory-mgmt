package com.sri.inventory.mgmt;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfitRepository extends JpaRepository<Profit, Long> {
    Profit findTop1ByOrderByIdDesc();
}
