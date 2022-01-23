package com.magicbricks.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Table(value = "tpshort_url")
@NoArgsConstructor
public class TpshortUrl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long shorturlfnum;

	private LocalDateTime createdate;

	private Long createdby;

	private String fullurl;

	private LocalDateTime moddate;

	private LocalDateTime modidate;

	private String shorturl;

}