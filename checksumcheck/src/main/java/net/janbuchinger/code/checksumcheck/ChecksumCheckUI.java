/*
 *  Checksum Check, checksum creation and comparison ui
 *  
 *  Copyright (C) 2018  Jan Buchinger
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.janbuchinger.code.checksumcheck;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.codec.digest.DigestUtils;

import net.janbuchinger.code.mishmash.ui.UIFx;
import net.janbuchinger.code.mishmash.ui.userInput.FileDrop;
import net.janbuchinger.code.mishmash.ui.userInput.FilePathTextField;
import net.janbuchinger.code.mishmash.ui.userInput.JTextFieldWithPopUp;

public class ChecksumCheckUI implements ActionListener, DocumentListener {

	private final JFrame frm;

	private final String version = "0.1a";

	private final FilePathTextField ftFile;
	private final JTextFieldWithPopUp tfChecksum;
	private final JTextFieldWithPopUp tfChecksumTest;

	private final JButton btMd5;
	private final JButton btSha1;
	private final JButton btSha256;
	private final JButton btSha512;

	private final Color bgColorTextFieldNeutral;

	public ChecksumCheckUI() {
		frm = new JFrame("Checksum Check " + version);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ftFile = new FilePathTextField();
		ftFile.getTextField().getDocument().addDocumentListener(this);
		tfChecksum = new JTextFieldWithPopUp();
		bgColorTextFieldNeutral = tfChecksum.getBackground();
		tfChecksum.setEditable(false);
		tfChecksum.getDocument().addDocumentListener(this);
		tfChecksumTest = new JTextFieldWithPopUp();
		tfChecksumTest.getDocument().addDocumentListener(this);

		btMd5 = new JButton("MD5");
		btMd5.addActionListener(this);
		new FileDrop(btMd5, new FileDrop.Listener() {
			@Override
			public void filesDropped(File[] files) {
				if (files.length > 0) {
					ftFile.setPath(files[0].getPath());
					md5(files[0]);
				}
			}
		});
		btSha1 = new JButton("SHA-1");
		btSha1.addActionListener(this);
		new FileDrop(btSha1, new FileDrop.Listener() {
			@Override
			public void filesDropped(File[] files) {
				if (files.length > 0) {
					ftFile.setPath(files[0].getPath());
					sha1(files[0]);
				}
			}
		});
		btSha256 = new JButton("SHA-256");
		btSha256.addActionListener(this);
		new FileDrop(btSha256, new FileDrop.Listener() {
			@Override
			public void filesDropped(File[] files) {
				if (files.length > 0) {
					ftFile.setPath(files[0].getPath());
					sha256(files[0]);
				}
			}
		});
		btSha512 = new JButton("SHA-512");
		btSha512.addActionListener(this);
		new FileDrop(btSha512, new FileDrop.Listener() {
			@Override
			public void filesDropped(File[] files) {
				if (files.length > 0) {
					ftFile.setPath(files[0].getPath());
					sha512(files[0]);
				}
			}
		});

		JPanel pnButtons = new JPanel(new GridLayout(2, 2));
		pnButtons.add(btMd5);
		pnButtons.add(btSha1);
		pnButtons.add(btSha256);
		pnButtons.add(btSha512);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = UIFx.initGridBagConstraints();
		contentPane.add(new JLabel("File"), c);
		c.gridx++;
		contentPane.add(ftFile, c);
		c.gridx = 0;
		c.gridy++;
		contentPane.add(new JLabel("Checksum"), c);
		c.gridx++;
		contentPane.add(tfChecksum, c);
		c.gridx = 0;
		c.gridy++;
		contentPane.add(new JLabel("Testsum"), c);
		c.gridx++;
		contentPane.add(tfChecksumTest, c);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		contentPane.add(pnButtons, c);

		frm.setContentPane(contentPane);

		UIFx.packAndCenter(frm);
		frm.setVisible(true);
	}

	private final void md5(File f) {
		String s = "";
		try (InputStream data = new FileInputStream(f)) {
			s = DigestUtils.md5Hex(data);
		} catch (IOException e) {
			s = "Error";
		}
		tfChecksum.setText(s);
	}

	private final void sha1(File f) {
		String s = "";
		try (InputStream data = new FileInputStream(f)) {
			s = DigestUtils.sha1Hex(data);
		} catch (IOException e) {
			s = "Error";
		}
		tfChecksum.setText(s);
	}

	private final void sha256(File f) {
		String s = "";
		try (InputStream data = new FileInputStream(f)) {
			s = DigestUtils.sha256Hex(data);
		} catch (IOException e) {
			s = "Error";
		}
		tfChecksum.setText(s);
	}

	private final void sha512(File f) {
		String s = "";
		try (InputStream data = new FileInputStream(f)) {
			s = DigestUtils.sha512Hex(data);
		} catch (IOException e) {
			s = "Error";
		}
		tfChecksum.setText(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ftFile.getPath().length() == 0) {
			return;
		} else if (e.getSource() == btMd5) {
			md5(new File(ftFile.getPath()));
		} else if (e.getSource() == btSha1) {
			sha1(new File(ftFile.getPath()));
		} else if (e.getSource() == btSha256) {
			sha256(new File(ftFile.getPath()));
		} else if (e.getSource() == btSha512) {
			sha512(new File(ftFile.getPath()));
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (e.getDocument() == ftFile.getTextField().getDocument()) {
			tfChecksum.setText("");
			tfChecksum.setBackground(bgColorTextFieldNeutral);
			tfChecksumTest.setBackground(bgColorTextFieldNeutral);
		} else {
			if (tfChecksum.getText().equals(tfChecksumTest.getText())) {
				tfChecksum.setBackground(Color.green);
				tfChecksumTest.setBackground(Color.green);
			} else {
				tfChecksum.setBackground(Color.orange);
				tfChecksumTest.setBackground(Color.orange);
			}
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {}

	@Override
	public void changedUpdate(DocumentEvent e) {}
}
